package beauty.shafran.network.auth.jwt

import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.AuthenticationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.data.AuthorizedAccountData
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.companies.repository.CompaniesRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.core.annotation.Single

@Single
class JwtAuthenticationValidator(
    private val accountsRepository: AccountsRepository,
    private val accountSessionsRepository: AccountSessionsRepository,
    private val companiesRepository: CompaniesRepository,
) : AuthenticationValidator {
    override suspend operator fun invoke(call: ApplicationCall, credentials: JWTCredential): Principal? {
        val sessionId = credentials["sessionId"] ?: return null
        val session = accountSessionsRepository.findSessionWithId(sessionId)
        return coroutineScope {
            val account = async { accountsRepository.findAccountById(session.accountId) }
            val members = async { companiesRepository.findAccountMembersForAccount(session.accountId) }
            AuthorizedAccount(
                accountId = session.accountId,
                data = AuthorizedAccountData(account.await().data.username),
                scopes = members.await().flatMap { it.formattedAccessScopes }
            )
        }
    }
}
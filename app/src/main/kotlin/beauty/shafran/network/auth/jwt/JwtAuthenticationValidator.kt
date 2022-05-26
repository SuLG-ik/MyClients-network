package beauty.shafran.network.auth.jwt

import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.AuthenticationValidator
import beauty.shafran.network.auth.data.AuthorizedAccount
import beauty.shafran.network.auth.data.AuthorizedAccountData
import beauty.shafran.network.auth.entity.AccountSessionId
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.repository.PermissionsRepository
import beauty.shafran.network.utils.Transactional
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


class JwtAuthenticationValidator(
    private val accountsRepository: AccountsRepository,
    private val accountSessionsRepository: AccountSessionsRepository,
    private val permissionsRepository: PermissionsRepository,
    private val transactional: Transactional,
) : AuthenticationValidator {

    override suspend operator fun invoke(call: ApplicationCall, credentials: JWTCredential): Principal? {
        return transactional.withSuspendedTransaction {
            val sessionId =
                credentials.payload.getClaim("sessionId").asLong() ?: return@withSuspendedTransaction null
            val session = accountSessionsRepository.run { findSessionWithId(AccountSessionId(sessionId)) }
            val account = transactionAsync { accountsRepository.run { findAccountById(session.accountId) } }
            val permissions = permissionsRepository.run { getPermissionsForSession(session.id) }
            AuthorizedAccount(
                accountId = session.accountId,
                data = AuthorizedAccountData(account.await().username),
                permissions = permissions.permissions
            )
        }
    }

}
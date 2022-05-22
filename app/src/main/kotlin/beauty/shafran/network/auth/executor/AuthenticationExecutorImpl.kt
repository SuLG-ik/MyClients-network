package beauty.shafran.network.auth.executor

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.data.LoginAccountRequest
import beauty.shafran.network.auth.data.LoginAccountResponse
import beauty.shafran.network.auth.data.RefreshTokenRequest
import beauty.shafran.network.auth.data.RefreshTokenResponse
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.token.TokenAuthService
import beauty.shafran.network.companies.repository.CompaniesRepository
import beauty.shafran.network.utils.Transactional
import org.koin.core.annotation.Single

@Single
class AuthenticationExecutorImpl(
    private val accountsRepository: AccountsRepository,
    private val companiesRepository: CompaniesRepository,
    private val accountSessionsRepository: AccountSessionsRepository,
    private val auth: TokenAuthService,
    private val transactional: Transactional,
) : AuthenticationExecutor {

    override suspend fun loginAccount(request: LoginAccountRequest): LoginAccountResponse {
        return when (request) {
            is LoginAccountRequest.UsernameAndPassword -> request.loginAccount()
        }
    }

    override suspend fun refreshAccount(request: RefreshTokenRequest): RefreshTokenResponse {
        return when (request) {
            is RefreshTokenRequest.JwtRefreshTokenRequest -> request.refreshAccount()
        }
    }

    private suspend fun RefreshTokenRequest.JwtRefreshTokenRequest.refreshAccount(): RefreshTokenResponse {
        return transactional.withSuspendedTransaction {
            val sessionId = auth.extractSessionId(data)
            val session = with(accountSessionsRepository) { findSessionWithId(sessionId) }
            val jwt = auth.generateTokenForSession(session)
            RefreshTokenResponse(
                accountId = session.accountId,
                token = jwt,
            )
        }
    }


    private suspend fun LoginAccountRequest.UsernameAndPassword.loginAccount(): LoginAccountResponse {
        return transactional.withSuspendedTransaction {
            val account = with(accountsRepository) { findAccountByUsernameCredential(username.lowercase(), password) }
            val session = with(accountSessionsRepository) { createSessionForAccount(AccountId(account.id)) }
            val jwt = auth.generateTokenForSession(session)
            LoginAccountResponse(
                accountId = AccountId(account.id),
                token = jwt,
            )
        }
    }

}
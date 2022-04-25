package beauty.shafran.network.auth.executor

import beauty.shafran.network.account.entity.AccountEntityData
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.token.TokenAuthService
import beauty.shafran.network.companies.repository.CompaniesRepository
import org.springframework.stereotype.Component

@Component
class AuthenticationExecutorImpl(
    private val accountsRepository: AccountsRepository,
    private val companiesRepository: CompaniesRepository,
    private val accountSessionsRepository: AccountSessionsRepository,
    private val auth: TokenAuthService,
) : AuthenticationExecutor {

    override suspend fun registerAccount(request: RegisterAccountRequest): RegisterAccountResponse {
        return when (request) {
            is RegisterAccountRequest.UsernameAndPassword -> request.registerAccount()
        }
    }

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

    private suspend fun RegisterAccountRequest.UsernameAndPassword.registerAccount(): RegisterAccountResponse {
        val account =
            accountsRepository.createAccount(AccountEntityData(username = username, name = "name"), password = password)
        val session = accountSessionsRepository.createSessionForAccount(account.id.toString())
        val jwt = auth.generateTokenForSession(session)
        return RegisterAccountResponse(
            accountId = account.id.toString(),
            token = jwt,
        )
    }


    private suspend fun RefreshTokenRequest.JwtRefreshTokenRequest.refreshAccount(): RefreshTokenResponse {
        val session = auth.findSessionWithRefreshToken(data)
        val jwt = auth.generateTokenForSession(session)
        return RefreshTokenResponse(
            accountId = session.accountId,
            token = jwt,
        )
    }


    private suspend fun LoginAccountRequest.UsernameAndPassword.loginAccount(): LoginAccountResponse {
        val account = accountsRepository.findAccountByUsernameCredential(username, password)
        val session = accountSessionsRepository.createSessionForAccount(account.id.toString())
        val jwt = auth.generateTokenForSession(session)
        return LoginAccountResponse(
            accountId = account.id.toString(),
            token = jwt,
        )
    }

}
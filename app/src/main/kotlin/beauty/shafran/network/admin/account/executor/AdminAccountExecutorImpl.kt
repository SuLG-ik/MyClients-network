package beauty.shafran.network.admin.account.executor

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.account.repository.AccountsRepository
import beauty.shafran.network.auth.data.RegisterAccountRequest
import beauty.shafran.network.auth.data.RegisterAccountResponse
import beauty.shafran.network.auth.repository.AccountSessionsRepository
import beauty.shafran.network.auth.token.TokenAuthService
import beauty.shafran.network.utils.Transactional
import org.koin.core.annotation.Single

@Single
class AdminAccountExecutorImpl(
    private val accountsRepository: AccountsRepository,
    private val accountSessionsRepository: AccountSessionsRepository,
    private val authService: TokenAuthService,
    private val transactional: Transactional,
) : AdminAccountExecutor {
    override suspend fun registerAccount(request: RegisterAccountRequest): RegisterAccountResponse {
        return when (request) {
            is RegisterAccountRequest.UsernameAndPassword -> request.registerAccount()
        }
    }

    private suspend fun RegisterAccountRequest.UsernameAndPassword.registerAccount(): RegisterAccountResponse {
        return transactional.withSuspendedTransaction {
            val account =
                accountsRepository.run {
                    createAccount(
                        username = username,
                        password = password)
                }
            val session = accountSessionsRepository.run { createSessionForAccount(AccountId(account.id)) }
            val jwt = authService.generateTokenForSession(session)
            RegisterAccountResponse(
                accountId = AccountId(account.id),
                token = jwt,
            )
        }
    }
}
package beauty.shafran.network.auth.route

import beauty.shafran.network.AccessDenied
import beauty.shafran.network.accounts.data.*
import beauty.shafran.network.accounts.repository.AccountRepository
import beauty.shafran.network.auth.AuthorizedAccount
import beauty.shafran.network.auth.RefreshAuthorizedAccount
import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountSessionId
import beauty.shafran.network.auth.data.*
import beauty.shafran.network.auth.jwt.Verifier
import beauty.shafran.network.auth.repository.AuthPasswordRepository
import beauty.shafran.network.auth.token.AuthTokenService
import beauty.shafran.network.database.Transactional
import beauty.shafran.network.database.invoke

internal class AuthRouterImpl(
    private val service: AuthTokenService,
    private val accountRepository: AccountRepository,
    private val authPassword: AuthPasswordRepository,
    private val transactional: Transactional,
    private val verifier: Verifier,
) : AuthRouter {

    override suspend fun refresh(request: RefreshTokenRequest, account: AuthorizedAccount): RefreshTokenResponse {
        return transactional {
            if (account !is RefreshAuthorizedAccount)
                throw AccessDenied()
            when (request) {
                is RefreshTokenRequest.JwtRefreshTokenRequest -> {
                    val token = verifier.verifyIgnoreExpire(request.accessToken)
                    RefreshTokenResponse(
                        service.refreshTokens(
                            tokenId = TokenId(account.tokenId),
                            accountId = AccountId(account.accountId),
                            sessionId = AccountSessionId(token.getClaim("sessionId").asLong()),
                            authorities = token.getClaim("authorities").asList(String::class.java),
                        )
                    )
                }
            }
        }
    }

    override suspend fun login(request: LoginAccountRequest): LoginAccountResponse {
        return transactional {
            when (request) {
                is LoginAccountRequest.UsernameAndPassword -> {
                    val account = accountRepository.findAccount(request.username)
                    val tokens =
                        service.generateTokens(accountId = AccountId(account.id), authorities = listOf("ROLE_user"))
                    if (!authPassword.matchPassword(AccountId(account.id), request.password)) {
                        throw AccessDenied()
                    }
                    LoginAccountResponse(
                        accountId = AccountId(account.id),
                        tokens = tokens,
                    )
                }
            }
        }
    }

    override suspend fun register(request: RegisterAccountRequest): RegisterAccountResponse {
        return transactional {
            when (request) {
                is RegisterAccountRequest.UsernameAndPassword -> {
                    val account = accountRepository.saveAccount(request.username, request.name)
                    authPassword.setPassword(accountId = AccountId(account.id), rawPassword = request.password)
                    val tokens =
                        service.generateTokens(accountId = AccountId(account.id), authorities = listOf("ROLE_user"))
                    if (!authPassword.matchPassword(AccountId(account.id), request.password)) {
                        throw AccessDenied()
                    }
                    RegisterAccountResponse(
                        accountId = AccountId(account.id),
                        tokens = tokens,
                    )
                }
            }
        }
    }

}
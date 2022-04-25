package beauty.shafran.network.auth.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AccountAuthorizationRequest(
    val accessToken: AccessTokenData,
)

@Serializable
class AccountAuthorizationResponse(
    val account: AuthorizedAccount,
)

@Serializable
data class AuthorizedAccount(
    val accountId: String,
    val data: AuthorizedAccountData,
    val scopes: List<String>,
)

@Serializable
data class AuthorizedAccountData(
    val username: String,
)

@Serializable
sealed class RefreshTokenRequest {

    @Serializable
    @SerialName("refresh_jwt")
    class JwtRefreshTokenRequest(
        val data: JwtRefreshTokenData,
    ) : RefreshTokenRequest()

}


@Serializable
class RefreshTokenResponse(
    val accountId: String,
    val token: TokenData,
)

@Serializable
sealed class RegisterAccountRequest {

    @Serializable
    @SerialName("username_password")
    class UsernameAndPassword(
        val username: String,
        val password: String,
    ) : RegisterAccountRequest()

}

@Serializable
class RegisterAccountResponse(
    val accountId: String,
    val token: TokenData,
)

@Serializable
sealed class LoginAccountRequest {

    @Serializable
    @SerialName("username_password")
    class UsernameAndPassword(
        val username: String,
        val password: String,
    ) : LoginAccountRequest()

}

@Serializable
class LoginAccountResponse(
    val accountId: String,
    val token: TokenData,
)
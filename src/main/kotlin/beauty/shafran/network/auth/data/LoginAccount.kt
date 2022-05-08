package beauty.shafran.network.auth.data

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
class AccountAuthorizationRequest(
    val accessToken: AccessTokenData,
) : Parcelable

@Serializable
@Parcelize
class AccountAuthorizationResponse(
    val account: AuthorizedAccount,
) : Parcelable

@Serializable
@Parcelize
data class AuthorizedAccount(
    val accountId: String,
    val data: AuthorizedAccountData,
    val scopes: List<String>,
) : Parcelable, Principal

@Serializable
@Parcelize
data class AuthorizedAccountData(
    val username: String,
) : Parcelable

@Serializable
sealed class RefreshTokenRequest : Parcelable {

    @Parcelize
    @Serializable
    @SerialName("refresh_jwt")
    class JwtRefreshTokenRequest(
        val data: JwtRefreshTokenData,
    ) : RefreshTokenRequest()

}


@Serializable
@Parcelize
class RefreshTokenResponse(
    val accountId: String,
    val token: TokenData,
) : Parcelable

@Serializable
sealed class RegisterAccountRequest : Parcelable {

    @SerialName("username_password")
    @Parcelize
    @Serializable
    class UsernameAndPassword(
        val username: String,
        val password: String,
    ) : RegisterAccountRequest()

}

@Serializable
@Parcelize
class RegisterAccountResponse(
    val accountId: String,
    val token: TokenData,
) : Parcelable

@Serializable
sealed class LoginAccountRequest : Parcelable {

    @Parcelize
    @Serializable
    @SerialName("username_password")
    class UsernameAndPassword(
        val username: String,
        val password: String,
    ) : LoginAccountRequest()
}

@Serializable
@Parcelize
class LoginAccountResponse(
    val accountId: String,
    val token: TokenData,
) : Parcelable

package beauty.shafran.network.auth.data

import beauty.shafran.network.account.data.AccountId
import beauty.shafran.network.auth.entity.PermissionEntity
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import io.ktor.server.auth.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class AuthorizedAccount(
    val accountId: AccountId,
    val data: AuthorizedAccountData,
    val permissions: List<PermissionEntity>,
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
    val accountId: AccountId,
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
    val accountId: AccountId,
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
    val accountId: AccountId,
    val token: TokenData,
) : Parcelable

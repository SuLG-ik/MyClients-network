package beauty.shafran.network.auth.data

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountUsername
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class AccountPassword(val value: String)

@Serializable
sealed class LoginAccountRequest {

    @Serializable
    @SerialName("username_password")
    class UsernameAndPassword(
        val username: AccountUsername,
        val password: AccountPassword,
    ) : LoginAccountRequest()
}

@Serializable
class LoginAccountResponse(
    val accountId: AccountId,
    val tokens: TokensData,
)

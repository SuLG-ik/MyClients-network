package beauty.shafran.network.auth.data

import beauty.shafran.network.accounts.data.AccountId
import beauty.shafran.network.accounts.data.AccountUsername
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
sealed class RegisterAccountRequest {

    @SerialName("username_password")
    @Serializable
    class UsernameAndPassword(
        val username: AccountUsername,
        val password: AccountPassword,
        val name: String,
    ) : RegisterAccountRequest()

}

@Serializable
class RegisterAccountResponse(
    val accountId: AccountId,
    val tokens: TokensData,
)


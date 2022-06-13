package beauty.shafran.network.accounts.data

import beauty.shafran.network.NetworkException
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("account_with_id_not_exists")
class AccountWithIdDoesNotExists(val accountId: AccountId) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}

@Serializable
@SerialName("account_with_username_not_exists")
class AccountWithUsernameDoesNotExists(val username: AccountUsername) : NetworkException() {
    override val httpStatusCode: HttpStatusCode
        get() = HttpStatusCode.NotFound
}
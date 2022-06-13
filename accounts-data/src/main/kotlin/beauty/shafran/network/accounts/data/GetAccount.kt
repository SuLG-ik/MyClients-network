package beauty.shafran.network.accounts.data

import kotlinx.serialization.Serializable


@Serializable
data class GetAccountRequest(
    val accountId: AccountId? = null,
)

@Serializable
data class GetAccountResponse(
    val account: Account,
)
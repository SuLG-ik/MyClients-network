package beauty.shafran.network.accounts.data

import kotlinx.serialization.Serializable


@Serializable
@JvmInline
value class AccountId(val value: Long)

@JvmInline
@Serializable
value class AccountSessionId(val value: Long)

@JvmInline
@Serializable
value class AccountUsername(val value: String)


@Serializable
data class Account(
    val id: AccountId,
    val username: AccountUsername,
    val data: AccountData,
)

@Serializable
data class AccountData(
    val name: String,
)
package beauty.shafran.network.customers.data

import kotlinx.serialization.Serializable

@Serializable
data class CustomersStorage(
    val id: CustomersStorageId,
)

@Serializable
@JvmInline
value class CustomersStorageId(val id: Long)
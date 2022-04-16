package beauty.shafran.network.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class AccountCredentials(
    val username: String,
    val password: String,
)

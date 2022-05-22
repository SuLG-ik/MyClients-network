package beauty.shafran.network.utils

import kotlinx.serialization.Serializable

@Serializable
data class PagedData(
    val page: Int,
    val offset: Int,
)

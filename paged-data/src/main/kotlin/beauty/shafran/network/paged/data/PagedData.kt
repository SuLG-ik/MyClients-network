package beauty.shafran.network.paged.data

import kotlinx.serialization.Serializable

@Serializable
data class PagedData(
    val limit: Int,
    val start: Long,
)
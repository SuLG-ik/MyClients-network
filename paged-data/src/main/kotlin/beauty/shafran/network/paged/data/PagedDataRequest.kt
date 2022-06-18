package beauty.shafran.network.paged.data

import kotlinx.serialization.Serializable

@Serializable
data class PagedDataRequest(
    val limit: Int,
    val start: Long,
)

@Serializable
data class PagedDataResponse(
    val limit: Int,
    val start: Long,
    val nextStart: Long? = null,
)

fun PagedDataRequest.toResponse(currentResponseSize: Int?): PagedDataResponse {
    return toResponse(currentResponseSize == limit)
}

fun PagedDataRequest.toResponse(nextPageExists: Boolean): PagedDataResponse {
    return PagedDataResponse(limit, start, if (nextPageExists) start + limit else null)
}
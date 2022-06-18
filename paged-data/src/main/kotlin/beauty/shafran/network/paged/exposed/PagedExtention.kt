package beauty.shafran.network.paged.exposed

import beauty.shafran.network.paged.data.PagedDataRequest
import org.jetbrains.exposed.sql.Query

fun Query.paged(pagedData: PagedDataRequest?): Query {
    if (pagedData != null)
        limit(n = pagedData.limit, offset = pagedData.start)
    return this
}
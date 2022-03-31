package beauty.shafran.network.utils

import org.litote.kmongo.coroutine.CoroutineFindPublisher


fun <T : Any> CoroutineFindPublisher<T>.paged(offset: Int, page: Int): CoroutineFindPublisher<T> =
    this.skip(offset * page).limit(offset)

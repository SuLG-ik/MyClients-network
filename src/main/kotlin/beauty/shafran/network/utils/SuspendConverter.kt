package beauty.shafran.network.utils

import kotlinx.coroutines.reactor.mono
import org.springframework.core.convert.converter.Converter
import reactor.core.publisher.Mono

abstract class SuspendConverter<S, T> : Converter<S, Mono<T>> {

    override fun convert(source: S): Mono<T> {
        return mono { source.convert() }
    }

    protected abstract suspend fun S.convert(): T

}

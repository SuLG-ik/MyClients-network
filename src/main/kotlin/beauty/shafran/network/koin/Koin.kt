package beauty.shafran.network.koin

import io.ktor.events.EventDefinition
import io.ktor.server.application.*
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin


val KoinPlugin = createApplicationPlugin("Koin", { KoinApplication.init() }) {
    val monitor = environment?.monitor
    monitor?.subscribe(ApplicationStopped) {
        monitor.raise(KoinApplicationStopPreparing, pluginConfig)
        stopKoin()
        monitor.raise(KoinApplicationStopped, pluginConfig)
    }

    startKoin(pluginConfig)
    monitor?.raise(KoinApplicationStarted, pluginConfig)
}


val KoinApplicationStarted = EventDefinition<KoinApplication>()

/**
 * Event definition for an event that is fired when the [KoinApplication] is going to stop
 */
val KoinApplicationStopPreparing = EventDefinition<KoinApplication>()

/**
 * Event definition for [KoinApplication] Stopping event
 */
val KoinApplicationStopped = EventDefinition<KoinApplication>()
package app.e20.api.plugins

import app.e20.config.ApplicationConfig
import app.e20.core.clients.BrevoClient
import app.e20.core.clients.RedisClient
import app.e20.di.ClientModule
import app.e20.di.DataModule
import app.e20.di.IClosableComponent
import app.e20.di.LogicModule
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import org.koin.core.logger.Level
import org.koin.ksp.generated.module
import org.koin.ktor.ext.getKoin
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import org.koin.ktor.plugin.KoinApplicationStopped
import org.koin.logger.slf4jLogger

private val logger = KotlinLogging.logger {  }

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger(Level.valueOf(ApplicationConfig.logLevel.levelStr))

        modules(LogicModule().module, ClientModule().module, DataModule().module)

        this.createEagerInstances()
    }

    environment.monitor.subscribe(KoinApplicationStarted) {
        logger.info { "Koin application started" }
    }

    environment.monitor.subscribe(KoinApplicationStopPreparing) {
        logger.info { "Shutdown started" }

        val closableComponents by lazy {
            getKoin().getAll<IClosableComponent>()
        }

        closableComponents.forEach {
            runBlocking {
                it.close()
            }
        }
    }

    environment.monitor.subscribe(KoinApplicationStopped) {
        logger.info { "Shutdown completed gracefully" }
    }
}
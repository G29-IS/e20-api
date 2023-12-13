package app.e_20.api.plugins

import app.e_20.config.ApplicationConfig
import app.e_20.core.clients.BrevoClient
import app.e_20.data.sources.cache.RedisClient
import app.e_20.di.logicModule
import app.e_20.di.userModule
import io.ktor.server.application.*
import org.koin.core.logger.Level
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.KoinApplicationStarted
import org.koin.ktor.plugin.KoinApplicationStopPreparing
import org.koin.ktor.plugin.KoinApplicationStopped
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger(Level.valueOf(ApplicationConfig.logLevel.levelStr))
        modules(userModule, logicModule)
    }

    environment.monitor.subscribe(KoinApplicationStarted) {
        log.info("Koin application started")
    }

    environment.monitor.subscribe(KoinApplicationStopPreparing) {
        log.info("Shutdown started")

        val redisClient by inject<RedisClient>()
        val brevoClient by inject<BrevoClient>()

        redisClient.close()
        log.info("[1/3] Shutting down redis client")

        brevoClient.close()
        log.info("[2/3] Shutting down brevo client")

        log.info("[3/3] Shutting down web server")
    }

    environment.monitor.subscribe(KoinApplicationStopped) {
        log.info("Shutdown completed gracefully")
    }
}
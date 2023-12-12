package app.e_20

import app.e_20.api.plugins.*
import app.e_20.api.routing.configureRouting
import app.e_20.config.ApiConfig
import app.e_20.config.ApplicationConfig
import app.e_20.config.core.ConfigurationManager
import app.e_20.config.core.ConfigurationReader
import app.e_20.core.clients.BrevoClient
import app.e_20.data.sources.cache.RedisClient
import app.e_20.data.sources.db.PostgresClient
import ch.qos.logback.classic.Logger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

private val logger = KotlinLogging.logger { }

fun main() {
    /**
     * Load configuration properties (environment)
     */
    val configInitializer = ConfigurationManager(
        packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
        ConfigurationReader::read
    )

    configInitializer.initialize()


    /**
     * Configure logging
     */
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = ApplicationConfig.logLevel


    /**
     * Initialize objects
     * If objects aren't called they initialize lazily which can lead to a false positive ready state
     */
    runBlocking {
        PostgresClient.init()
    }
    RedisClient

    /**
     * Launch api server
     */
    val apiServer = embeddedServer(Netty, port = ApiConfig.port, host = "0.0.0.0", module = Application::indexApplicationModule)

    // Add shutdown hook to api server
    apiServer.addShutdownHook {
        logger.info { "[1/3] Shutting down web server" }
    }

    /**
     * Configure application shutdown hook
     */
    Runtime.getRuntime().addShutdownHook(
        Thread {
            logger.info { "Shutdown started" }

            logger.info { "[1/3] Api server shutdown" }

            BrevoClient.close()
            logger.info { "[2/3] SendinblueClient client shutdown" }

            RedisClient.close()
            logger.info { "[3/3] RedisClient client shutdown" }

            logger.info { "Shutdown successful, bye bye ^^" }
        }
    )

    apiServer.start(wait = true)
}

private fun Application.indexApplicationModule() {
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureValidator()
    configureRouting()
    configureSwagger()
}

package app.e20.api

import app.e20.config.ApiConfig
import app.e20.config.core.ConfigurationManager
import app.e20.config.core.ConfigurationReader
import app.e20.core.logic.typedId.serialization.IdKotlinXSerializationModule
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiTestUtilities {
    const val DEFAULT_USER_ID = "bc9e631a-5e19-4885-ac01-1a3c48ffe9d3"
    const val DEFAULT_USER_EMAIL = "default@gmail.com"
    const val DEFAULT_USER_PASSWORD = "g29-password"
    val httpClient: HttpClient

    init {
        /**
         * Load configuration properties (environment)
         */
        val configInitializer = ConfigurationManager(
            packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
            ConfigurationReader::read
        )

        configInitializer.initialize()

        httpClient = HttpClient(Apache) {
            install(Logging) {
                level = LogLevel.NONE
            }
            install(ContentNegotiation) {
                json(Json {
                    serializersModule = IdKotlinXSerializationModule
                })
            }
            install(Resources) {
                serializersModule = IdKotlinXSerializationModule
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                url("http://localhost:${ApiConfig.port}/")
            }
        }
    }
}
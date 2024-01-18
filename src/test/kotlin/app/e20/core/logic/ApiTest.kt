package app.e20.core.logic

import app.e20.api.routing.auth.LoginRoute
import app.e20.config.ApiConfig
import app.e20.config.core.ConfigurationManager
import app.e20.config.core.ConfigurationReader
import app.e20.data.models.auth.LoginCredentials
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class ApiTest {
    private val httpClient: HttpClient
    private var authToken: String? = null

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
            install(Logging)
            install(ContentNegotiation) {
                json(Json)
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)
                url("http://localhost:${ApiConfig.port}/")
            }
        }
    }

    @Test
    fun testLoginRoute() {
        runBlocking {
            val response = httpClient.post("/login") {
                setBody(LoginCredentials("default@gmail.com", "default"))
            }

            @Serializable
            data class LoginResponse(val token: String)

            authToken = try {
                response.body<LoginResponse>().token
            } catch (e: Exception) {
                null
            }

            assert(authToken != null)
        }
    }
}
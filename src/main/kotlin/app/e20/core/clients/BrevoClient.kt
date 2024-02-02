package app.e20.core.clients

import app.e20.config.BrevoConfig
import app.e20.data.models.brevo.BrevoCodeOperationRequestBody
import app.e20.data.models.brevo.BrevoGenericRequestBody
import app.e20.di.IClosableComponent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

private val log = KotlinLogging.logger { }

/**
 * Client to interact with the Brevo api
 *
 * @see sendPasswordResetEmail
 * @see sendPasswordResetSuccessEmail
 */
@Single(createdAtStart = true)
class BrevoClient: IClosableComponent {

    // Cannot set default url and header if injected with DI
    private val httpClient = HttpClient(Apache) {
        install(Logging)
        install(ContentNegotiation) {
            json(Json)
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            exponentialDelay()
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            url("https://api.sendinblue.com/v3/")
            header("api-key", BrevoConfig.apiKey)
        }
    }

    /**
     * Sends a password reset message to a given [email]
     *
     * @param email The target receiver email
     * @param token The token for the password reset
     */
    suspend fun sendPasswordResetEmail(email: String, token: String): Boolean {
        val response: HttpResponse = httpClient.post("smtp/email") {
            setBody(BrevoCodeOperationRequestBody(
                to = listOf(
                    BrevoGenericRequestBody.To(
                        email = email
                    )
                ),
                templateId = 1,
                params = BrevoCodeOperationRequestBody.Params(
                    url = "${BrevoConfig.passwordResetUrl}?token=${token}"
                )
            ))
        }

        if (response.status.isSuccess()) {
            log.debug { "Sent password reset email to $email" }
        } else {
            log.error { "Failed to send password reset email\nResponse: $response" }
        }

        return response.status.isSuccess()
    }

    /**
     * Sends an email stating that the password has been successfully reset
     *
     * @param email The target receiver email
     */
    suspend fun sendPasswordResetSuccessEmail(email: String): Boolean {
        val response: HttpResponse = httpClient.post("smtp/email") {
            setBody(BrevoGenericRequestBody(
                to = listOf(
                    BrevoGenericRequestBody.To(
                        email = email
                    )
                ),
                templateId = 2
            ))
        }

        if (response.status.isSuccess()) {
            log.debug { "Sent password reset success email to $email" }
        } else {
            log.error { "Failed to send password reset success email\nResponse: $response" }
        }

        return response.status.isSuccess()
    }

    override suspend fun close() {
        httpClient.close()
    }
}
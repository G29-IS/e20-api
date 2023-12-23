package app.e20.core.clients

import app.e20.config.BrevoConfig
import app.e20.data.models.brevo.BrevoCodeOperationRequestBody
import app.e20.data.models.brevo.BrevoGenericRequestBody
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.koin.core.annotation.Single

private val log = KotlinLogging.logger { }

/**
 * Client to interact with the Brevo api
 *
 * @see sendPasswordResetEmail
 * @see sendPasswordResetSuccessEmail
 */
@Single(createdAtStart = true)
class BrevoClient(
    private val httpClient: HttpClient
) {
    init {
        httpClient.config {
            defaultRequest {
                url("https://api.sendinblue.com/v3/")
                header("api-key", BrevoConfig.apiKey)
            }
        }
    }

    /**
     * Sends a password reset message to a given [email]
     * TODO: Define template ids in Brevo
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
                templateId = 3
            ))
        }

        if (response.status.isSuccess()) {
            log.debug { "Sent password reset success email to $email" }
        } else {
            log.error { "Failed to send password reset success email\nResponse: $response" }
        }

        return response.status.isSuccess()
    }

    fun close() {
        httpClient.close()
    }
}

package app.e20.api

import app.e20.api.routing.auth.LoginRoute
import app.e20.data.models.auth.LoginCredentials
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class ApiAuthRoutesTest {
    private val httpClient = ApiTestUtilities.httpClient

    @Test
    fun `perform login with invalid credentials expect unauthenticated`() {
        runBlocking {
            val res = httpClient.post(LoginRoute()) {
                setBody(LoginCredentials(
                    email = "invalid@email.com",
                    password = "missing password"
                ))
            }

            assert(res.status.value == 401)
        }
    }

    // Successful login is already tested in the `ApiEventRoutesTest.kt` file
}
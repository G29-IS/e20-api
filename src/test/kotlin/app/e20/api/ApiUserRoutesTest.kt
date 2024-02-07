package app.e20.api

import app.e20.api.routing.auth.LoginRoute
import app.e20.api.routing.user.LogoutRoute
import app.e20.api.routing.user.MeRoute
import app.e20.api.routing.user.UserRoute
import app.e20.core.logic.typedId.newIxId
import app.e20.core.logic.typedId.toIxId
import app.e20.data.models.auth.LoginCredentials
import app.e20.data.models.user.UserData
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.*

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiUserRoutesTest {
    private val httpClient = ApiTestUtilities.httpClient
    private var authToken: String? = null
    private var selfUser: UserData? = null

    @Test
    @Order(1)
    fun `perform login expect auth token`() {
        runBlocking {
            val res = httpClient.post(LoginRoute()) {
                setBody(LoginCredentials(
                    email = ApiTestUtilities.DEFAULT_USER_EMAIL,
                    password = ApiTestUtilities.DEFAULT_USER_PASSWORD
                ))
            }

            @Serializable
            data class LoginResponse(val token: String)

            authToken = try {
                res.body<LoginResponse>().token
            } catch (e: Exception) {
                null
            }

            assert(authToken != null)
        }
    }

    @Test
    @Order(2)
    fun `get self user expect success`() {
        runBlocking {
            val res = httpClient.get(MeRoute()) {
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(res.status.value == 200)

            selfUser = try {
                res.body<UserData>()
            } catch (e: Exception) {
                fail(e)
            }

            assert(selfUser!!.email == ApiTestUtilities.DEFAULT_USER_EMAIL)
        }
    }

    @Test
    @Order(3)
    fun `get missing user with events expect not found`() {
        runBlocking {
            val res = httpClient.get(UserRoute(id = newIxId())) {
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(res.status.value == 404)
        }
    }

    @Test
    @Order(4)
    fun `perform logout expect success`() {
        runBlocking {
            val res = httpClient.get(LogoutRoute()) {
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(res.status.value == 200)
        }
    }
}
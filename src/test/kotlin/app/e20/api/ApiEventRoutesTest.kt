package app.e20.api

import app.e20.api.routing.auth.LoginRoute
import app.e20.api.routing.event.EventsRoute
import app.e20.config.ApiConfig
import app.e20.config.core.ConfigurationManager
import app.e20.config.core.ConfigurationReader
import app.e20.core.logic.typedId.serialization.IdKotlinXSerializationModule
import app.e20.data.models.auth.LoginCredentials
import app.e20.data.models.event.EventData
import app.e20.data.models.event.EventPlaceData
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.apache.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import java.util.*

/*
TODO:
- invalid event data test
- unauthenticated request test
- event not found request
 */
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiEventRoutesTest {
    private var authToken: String? = null

    private val eventCreateData = EventData.EventCreateOrUpdateRequestData(
        name = "testing-${UUID.randomUUID().toString().take(16)}",
        coverImageUrl = "missing-image",
        description = "test event",
        place = EventPlaceData("test location", "test address", null),
        openingDateTime = Clock.System.now().plus(24, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.UTC),
        doorOpeningDateTime = Clock.System.now().plus(25, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.UTC),
        type = EventData.EventType.PRIVATEPARTY,
        maxParticipants = 45,
        visibility = EventData.EventVisibility.PUBLIC,
        availability = EventData.EventAvailability.AVAILABLE,
        paymentLink = null
    )
    private var createdEvent: EventData? = null

    companion object {
        private lateinit var httpClient: HttpClient

        @JvmStatic
        @BeforeAll
        fun setup() {
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

    @Test
    @Order(1)
    fun `perform login expect auth token`() {
        runBlocking {
            val res = httpClient.post(LoginRoute()) {
                setBody(LoginCredentials("default@gmail.com", "default"))
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
    fun `create event expect success`() {
        runBlocking {
            val createEventRes = httpClient.post(EventsRoute()) {
                setBody(eventCreateData)
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(createEventRes.status.value == 200)

            createdEvent = try {
                createEventRes.body<EventData>()
            } catch (e: Exception) {
                fail(e)
            }

            assert(createdEvent?.name == eventCreateData.name)
        }
    }

    @Test
    @Order(3)
    fun `list event expect created event in list`() {
        runBlocking {
            val getAllRes = httpClient.get(EventsRoute()) {
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(getAllRes.status.value == 200)

            val allEvents = try {
                getAllRes.body<List<EventData>>()
            } catch (e: Exception) {
                fail(e)
            }

            assert(allEvents.any { it.name == eventCreateData.name })
        }
    }

    @Test
    @Order(4)
    fun `get created event expect success`() {
        runBlocking {
            val getRes = httpClient.get(
                EventsRoute.EventRoute(
                    parent = EventsRoute(),
                    id = createdEvent!!.idEvent
                )
            ) {
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(getRes.status.value == 200)

            val specificEvent = try {
                getRes.body<EventData>()
            } catch (e: Exception) {
                fail(e)
            }

            assert(specificEvent.name == eventCreateData.name)
        }
    }

    @Test
    @Order(5)
    fun `update event expect success`() {
        runBlocking {
            val eventUpdateData = eventCreateData.copy(
                name = "updated testing-${UUID.randomUUID().toString().take(16)}"
            )

            val updateRes = httpClient.put(
                EventsRoute.EventRoute(
                    parent = EventsRoute(),
                    id = createdEvent!!.idEvent
                )
            ) {
                headers {
                    bearerAuth(authToken!!)
                }
                setBody(eventUpdateData)
            }

            assert(updateRes.status.value == 200)

            val updatedEvent = try {
                updateRes.body<EventData>()
            } catch (e: Exception) {
                fail(e)
            }

            assert(updatedEvent.name == eventUpdateData.name)
        }
    }

    @Test
    @Order(6)
    fun `delete event expect success`() {
        runBlocking {
            val deleteEventRes = httpClient.delete(
                EventsRoute.EventRoute(
                    parent = EventsRoute(),
                    id = createdEvent!!.idEvent
                )
            ) {
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(deleteEventRes.status.value == 200)
        }
    }
}
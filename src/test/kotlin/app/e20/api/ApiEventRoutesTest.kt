package app.e20.api

import app.e20.api.routing.auth.LoginRoute
import app.e20.api.routing.event.EventsRoute
import app.e20.core.logic.typedId.newIxId
import app.e20.data.models.auth.LoginCredentials
import app.e20.data.models.event.EventData
import app.e20.data.models.event.EventPlaceData
import io.ktor.client.call.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import java.util.*

@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiEventRoutesTest {
    private val httpClient = ApiTestUtilities.httpClient
    private var authToken: String? = null

    private val eventCreateData = EventData.EventCreateOrUpdateRequestData(
        name = "testing-${UUID.randomUUID().toString().take(16)}",
        coverImageUrl = "missing-image",
        description = "test event",
        place = EventPlaceData("test location", "test address", null),
        openingDateTime = Clock.System.now().plus(24, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.UTC),
        doorOpeningDateTime = Clock.System.now().plus(25, DateTimeUnit.HOUR).toLocalDateTime(TimeZone.UTC),
        type = EventData.EventType.private_party,
        maxParticipants = 45,
        visibility = EventData.EventVisibility.public,
        availability = EventData.EventAvailability.available,
        paymentLink = null
    )
    private var createdEvent: EventData? = null

    @Test
    @Order(1)
    fun `list events no auth expect success`() {
        runBlocking {
            val getAllRes = httpClient.get(EventsRoute())

            assert(getAllRes.status.value == 200)
        }
    }

    @Test
    @Order(2)
    fun `create event no auth expect unauthenticated`() {
        runBlocking {
            val createEventRes = httpClient.post(EventsRoute()) {
                setBody(eventCreateData)
            }

            assert(createEventRes.status.value == 401)
        }
    }

    @Test
    @Order(3)
    fun `perform login expect auth token`() {
        runBlocking {
            val res = httpClient.post(LoginRoute()) {
                setBody(LoginCredentials(ApiTestUtilities.DEFAULT_USER_EMAIL, ApiTestUtilities.DEFAULT_USER_PASSWORD))
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
    @Order(4)
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
    @Order(5)
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
    @Order(6)
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
    @Order(7)
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
    @Order(8)
    fun `update event with invalid data expect bad request`() {
        runBlocking {
            val eventUpdateData = eventCreateData.copy(
                name = "updated testing-${UUID.randomUUID().toString().take(16)}",
                description = "",
                maxParticipants = -1
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

            assert(updateRes.status.value == 400)
        }
    }

    @Test
    @Order(9)
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

    @Test
    @Order(10)
    fun `create event with invalid data expect bad request`() {
        runBlocking {
            val createEventRes = httpClient.post(EventsRoute()) {
                setBody(
                    eventCreateData.copy(
                        maxParticipants = -1,
                        coverImageUrl = ""
                    )
                )
                headers {
                    bearerAuth(authToken!!)
                }
            }

            assert(createEventRes.status.value == 400)
        }
    }

    @Test
    @Order(11)
    fun `update missing event expect not found`() {
        runBlocking {
            val eventUpdateData = eventCreateData.copy(
                name = "unknown event",
            )

            val updateRes = httpClient.put(
                EventsRoute.EventRoute(
                    parent = EventsRoute(),
                    id = newIxId()
                )
            ) {
                headers {
                    bearerAuth(authToken!!)
                }
                setBody(eventUpdateData)
            }

            assert(updateRes.status.value == 404)
        }
    }
}
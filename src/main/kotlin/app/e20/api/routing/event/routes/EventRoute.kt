package app.e20.api.routing.event.routes

import app.e20.api.plugins.AuthenticationMethods
import app.e20.api.plugins.userIdFromSessionOrThrow
import app.e20.api.routing.event.EventsRoute
import app.e20.data.daos.event.EventDao
import app.e20.data.models.event.EventData
import io.github.smiley4.ktorswaggerui.dsl.resources.delete
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.put
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.eventRoute() {
    val eventDao by inject<EventDao>()

    get<EventsRoute.EventRoute>({
        tags = listOf("event")
        operationId = "get-event"
        summary = "gets a single event"
        request {
            pathParameter<String>("id") {
                required = true
                description = "the id of the event"
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "event found"
                body<EventData>()
            }

            HttpStatusCode.NotFound to {
                description = "event with the provided id not found"
            }
        }
    }) {
        val event = eventDao.get(it.id)
            ?: call.respond(HttpStatusCode.NotFound)

        call.respond(event)
    }

    authenticate(AuthenticationMethods.USER_SESSION_AUTH) {
        put<EventsRoute.EventRoute>({
            tags = listOf("event")
            operationId = "update-event"
            summary = "updates a single event"
            request {
                pathParameter<String>("id") {
                    required = true
                    description = "the id of the event"
                }
                body<EventData.EventCreateOrUpdateRequestData> {
                    description = "the new event data"
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "updated event"
                    body<EventData>()
                }

                HttpStatusCode.Unauthorized to {
                    description = "not logged in, only logged in users can update event"
                }

                HttpStatusCode.NotFound to {
                    description = "event doesn't exist or logged in user is not the organizer of the event"
                }
            }
        }) {
            val updateEventData = call.receive<EventData.EventCreateOrUpdateRequestData>()

            val updatedEvent = eventDao.update(
                id = it.id,
                organizerId = userIdFromSessionOrThrow(),
                eventCreateOrUpdateRequestData = updateEventData
            ) ?: call.respond(HttpStatusCode.NotFound)

            call.respond(updatedEvent)
        }

        delete<EventsRoute.EventRoute>({
            tags = listOf("event")
            operationId = "delete-event"
            summary = "deletes a single event"
            request {
                pathParameter<String>("id") {
                    required = true
                    description = "the id of the event"
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "event deleted"
                }

                HttpStatusCode.Unauthorized to {
                    description = "not logged in, only logged in users can delete events"
                }

                HttpStatusCode.NotFound to {
                    description = "event doesn't exist or logged in user is not the organizer of the event"
                }
            }
        }) {
            val deleted = eventDao.delete(
                id = it.id,
                organizerId = userIdFromSessionOrThrow()
            )

            if (deleted) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
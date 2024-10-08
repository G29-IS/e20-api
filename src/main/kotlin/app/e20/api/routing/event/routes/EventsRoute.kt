package app.e20.api.routing.event.routes

import app.e20.api.plugins.AuthenticationMethods
import app.e20.api.plugins.userIdFromSessionOrThrow
import app.e20.api.routing.event.EventsRoute
import app.e20.core.logic.DatetimeUtils
import app.e20.data.daos.event.EventDao
import app.e20.data.daos.user.UserDao
import app.e20.data.models.event.EventData
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.*
import org.koin.ktor.ext.inject

fun Route.eventsRoute() {
    val eventDao by inject<EventDao>()
    val userDao by inject<UserDao>()

    get<EventsRoute>({
        tags = listOf("event")
        operationId = "get-events"
        summary = "gets a list of events"
        request {
            queryParameter<LocalDateTime>("date-start") {
                description = "the starting date (in utc time zone) to filter events"
            }
            queryParameter<LocalDateTime>("date-end") {
                description = "the end date (in utc time zone) to filter events"
            }
            queryParameter<String>("location") {
                description = "the location filter for the events"
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "list of events"
                body<List<EventData>> {
                    description = "the list of available filtered events"
                }
            }
        }
    }) {
        val startDate = it.dateStart ?: DatetimeUtils.currentInstant()
            .minus(1, DateTimeUnit.YEAR, TimeZone.UTC)
            .toLocalDateTime(TimeZone.UTC)
        val endDate = it.dateEnd ?: DatetimeUtils.currentInstant()
            .plus(1, DateTimeUnit.YEAR, TimeZone.UTC)
            .toLocalDateTime(TimeZone.UTC)

        val events = eventDao.getForDates(startDate, endDate)
        val users = events.mapNotNull { event -> userDao.get(event.idOrganizer) }

        call.respond(EventData.EventsFeedResponse(
            events = events,
            users = users
        ))
    }

    authenticate(AuthenticationMethods.USER_SESSION_AUTH) {
        post<EventsRoute>({
            tags = listOf("event")
            operationId = "create-event"
            summary = "create a new event"
            request {
                body<EventData.EventCreateOrUpdateRequestData> {
                    description = "the event data for creation"
                }
            }
            response {
                HttpStatusCode.OK to {
                    description = "event created"
                    body<EventData>()
                }

                HttpStatusCode.Unauthorized to {
                    description = "not logged in, only logged in users can create events"
                }
            }
        }) {
            val eventCreateData = call.receive<EventData.EventCreateOrUpdateRequestData>()

            val createdEvent = eventDao.create(
                userId = userIdFromSessionOrThrow(),
                eventCreateOrUpdateRequestData = eventCreateData
            )

            call.respond(createdEvent)
        }
    }
}
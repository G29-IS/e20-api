package app.e20.api.routing.event.routes

import app.e20.api.plugins.AuthenticationMethods
import app.e20.api.plugins.userIdFromSessionOrThrow
import app.e20.api.routing.event.EventsRoute
import app.e20.core.logic.DatetimeUtils
import app.e20.data.daos.event.EventDao
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
            .minus(1, DateTimeUnit.MONTH, TimeZone.UTC)
            .toLocalDateTime(TimeZone.UTC)
        val endDate = it.dateEnd ?: DatetimeUtils.currentInstant()
            .plus(1, DateTimeUnit.MONTH, TimeZone.UTC)
            .toLocalDateTime(TimeZone.UTC)

        val events = eventDao.getForDates(startDate, endDate)

        call.respond(events)
    }

    authenticate(AuthenticationMethods.USER_SESSION_AUTH) {
        post<EventsRoute>({
            tags = listOf("event")
            operationId = "create-event"
            summary = "create a new event"
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
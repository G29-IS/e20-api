package app.e_20.api.routing.event.routes

import app.e_20.api.routing.event.EventsRoute
import app.e_20.data.models.event.EventDto
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.routing.*
import java.util.Date

fun Route.eventsRoute() {
    get<EventsRoute>({
        tags = listOf("event")
        operationId = "get-events"
        summary = "gets a list of events"
        request {
            queryParameter<Date>("date-start") {
                description = "" // TODO
            }
            queryParameter<Date>("date-end") {
                description = "" // TODO
            }
            queryParameter<String>("location") {
                description = "" // TODO
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "list of events"
                body<List<EventDto>> {
                    description = "" // TODO
                }
            }
        }
    }) {

    }

    post<EventsRoute>({
        tags = listOf("event")
        operationId = "create-event"
        summary = "create a new event"
    }) {

    }
}
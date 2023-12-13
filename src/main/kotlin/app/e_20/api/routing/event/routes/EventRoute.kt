package app.e_20.api.routing.event.routes

import app.e_20.api.routing.event.EventsRoute
import io.github.smiley4.ktorswaggerui.dsl.resources.delete
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.put
import io.ktor.server.routing.*

fun Route.eventRoute() {
    get<EventsRoute.EventRoute>({
        tags = listOf("event")
        operationId = "get-event"
        summary = "gets a single event"
    }) {

    }

    put<EventsRoute.EventRoute>({
        tags = listOf("event")
        operationId = "update-event"
        summary = "updates a single event"
    }) {

    }

    delete<EventsRoute.EventRoute>({
        tags = listOf("event")
        operationId = "delete-event"
        summary = "deletes a single event"
    }) {

    }
}
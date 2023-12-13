package app.e_20.api.routing.event.routes

import app.e_20.api.routing.event.EventsRoute
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.server.routing.*

fun Route.participationRoute() {
    get<EventsRoute.EventRoute.ParticipationRoute>({
        tags = listOf("event")
        operationId = "event-participation"
        summary = "marks participation to an event"
    }) {

    }
}
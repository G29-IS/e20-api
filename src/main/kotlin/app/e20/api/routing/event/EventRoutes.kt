package app.e20.api.routing.event

import app.e20.api.plugins.AuthenticationMethods
import app.e20.api.routing.event.routes.eventRoute
import app.e20.api.routing.event.routes.eventsRoute
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName

@Resource("/events")
class EventsRoute(
    @Contextual @SerialName("date-start") val dateStart: LocalDateTime? = null,
    @Contextual @SerialName("date-end") val dateEnd: LocalDateTime? = null,
    val location: String? = null
) {
    @Resource("/{id}")
    class EventRoute(val parent: EventsRoute, @Contextual val id: IxId<EventData>)
}

fun Route.eventRoutes() {
    eventsRoute()
    eventRoute()
}
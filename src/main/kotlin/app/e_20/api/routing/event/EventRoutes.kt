package app.e_20.api.routing.event

import app.e_20.api.routing.event.routes.eventRoute
import app.e_20.api.routing.event.routes.eventsRoute
import app.e_20.api.routing.event.routes.participationRoute
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.event.EventDto
import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.util.Date

@Resource("/events")
class EventsRoute(
    @Contextual @SerialName("date-start") val dateStart: Date? = null,
    @Contextual @SerialName("date-end") val dateEnd: Date? = null,
    val location: String? = null
) {
    @Resource("/{id}")
    class EventRoute(val parent: EventsRoute, @Contextual val id: IxId<EventDto>) {
        @Resource("/participation")
        class ParticipationRoute(val parent: EventRoute, val partecipate: Boolean? = null)
    }
}

fun Route.eventRoutes() {
    eventsRoute()
    eventRoute()
    participationRoute()
}
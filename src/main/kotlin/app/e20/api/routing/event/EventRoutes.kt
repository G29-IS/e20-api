package app.e20.api.routing.event

import app.e20.api.routing.event.routes.eventRoute
import app.e20.api.routing.event.routes.eventsRoute
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import io.ktor.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import java.util.*

@Resource("/events")
class EventsRoute(
    @Contextual @SerialName("date-start") val dateStart: Date? = null,
    @Contextual @SerialName("date-end") val dateEnd: Date? = null,
    val location: String? = null
) {
    @Resource("/{id}")
    class EventRoute(val parent: EventsRoute, @Contextual val id: IxId<EventData>) {
        @Resource("/participation")
        class ParticipationRoute(val parent: EventRoute, val partecipate: Boolean? = null)
    }
}

fun Route.eventRoutes() {
    eventsRoute()
    eventRoute()
    // participationRoute()
}
package app.e20.data.models.event

import app.e20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class EventData(
    @Contextual val id: IxId<EventData>
) {
    enum class EventType {
        CONCERT, FESTIVAL, BAR, CLUB, PARTY, HOUSEPARTY, PRIVATEPARTY, WORKSHOP, CONGRESS
    }

    enum class EventAvailability {
        AVAILABLE, RUNNING_OUT, SOLD_OUT, CANCELED
    }

    enum class EventVisibility {
        PUBLIC
    }
}
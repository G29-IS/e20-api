package app.e_20.data.models.event

import app.e_20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class EventDto(
    @Contextual val id: IxId<EventDto>
) {
    /*
    CONCERT
FESTIVAL
BAR
CLUB
PARTY
HOUSEPARTY
PRIVATEPARTY
WORKSHOP
CONGRESS
     */
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
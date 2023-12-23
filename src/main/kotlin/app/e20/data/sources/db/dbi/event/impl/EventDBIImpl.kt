package app.e20.data.sources.db.dbi.event.impl

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.event.EventDBI
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class EventDBIImpl : EventDBI {
    override suspend fun create(eventData: EventData) {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: IxId<EventData>): EventData? {
        TODO("Not yet implemented")
    }

    override suspend fun getForDates(startDate: LocalDateTime, endDate: LocalDateTime): List<EventData> {
        TODO("Not yet implemented")
    }


    override suspend fun update(
        id: IxId<EventData>,
        organizerId: IxId<UserData>,
        eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: IxId<EventData>, organizerId: IxId<UserData>): Boolean {
        TODO("Not yet implemented")
    }
}
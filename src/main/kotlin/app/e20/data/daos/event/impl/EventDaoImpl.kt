package app.e20.data.daos.event.impl

import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.newIxId
import app.e20.data.daos.event.EventDao
import app.e20.data.models.event.EventData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.event.EventDBI
import kotlinx.datetime.LocalDateTime
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class EventDaoImpl(
    private val eventDBI: EventDBI
) : EventDao {
    override suspend fun create(
        userId: IxId<UserData>,
        eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData
    ): EventData {
        val eventData = EventData(
            idEvent = newIxId(),
            name = eventCreateOrUpdateRequestData.name,
            coverImageUrl = eventCreateOrUpdateRequestData.coverImageUrl,
            idOrganizer = userId,
            description = eventCreateOrUpdateRequestData.description,
            place = eventCreateOrUpdateRequestData.place,
            openingDateTime = eventCreateOrUpdateRequestData.openingDateTime,
            doorOpeningDateTime = eventCreateOrUpdateRequestData.doorOpeningDateTime,
            type = eventCreateOrUpdateRequestData.type,
            maxParticipants = eventCreateOrUpdateRequestData.maxParticipants,
            visibility = eventCreateOrUpdateRequestData.visibility,
            availability = eventCreateOrUpdateRequestData.availability,
            paymentLink = eventCreateOrUpdateRequestData.paymentLink,
            isModified = false,
            timesShared = 0
        )

        eventDBI.create(eventData)

        return eventData
    }

    override suspend fun get(id: IxId<EventData>): EventData? {
        return eventDBI.get(id)
    }

    override suspend fun getForDates(startDate: LocalDateTime, endDate: LocalDateTime): List<EventData> {
        return eventDBI.getForDates(startDate, endDate)
    }

    override suspend fun update(id: IxId<EventData>, organizerId: IxId<UserData>, eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData): EventData? {
        val updated = eventDBI.update(id, organizerId, eventCreateOrUpdateRequestData)

        return if (updated) {
            get(id)
        } else {
            null
        }
    }

    override suspend fun delete(id: IxId<EventData>, organizerId: IxId<UserData>): Boolean {
        return eventDBI.delete(id, organizerId)
    }
}
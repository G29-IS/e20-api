package app.e20.data.sources.db.dbi.event.impl

import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.event.EventDBI
import app.e20.data.sources.db.schemas.event.*
import app.e20.data.sources.db.schemas.user.UsersTable
import app.e20.data.sources.db.toEntityId
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.update
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class EventDBIImpl : EventDBI {
    private fun userAndEventFilter(
        userId: IxId<UserData>,
        eventId: IxId<EventData>,
    ) = Op.build { (EventsTable.idOrganizer eq userId.toEntityId(UsersTable)) and (EventsTable.id eq eventId.toEntityId(EventsTable)) }

    override suspend fun create(eventData: EventData) {
        dbQuery {
            EventEntity.new(eventData.idEvent.id) {
                fromData(eventData)
            }

            EventPlaceEntity.new {
                fromData(eventData.idEvent, eventData.place)
            }
        }
    }

    override suspend fun get(id: IxId<EventData>): EventData? = dbQuery {
        EventEntity.find { EventsTable.id eq id.toEntityId(EventsTable) }
            .limit(1)
            .firstOrNull()
            ?.toData()
    }

    override suspend fun getOfOrganizer(id: IxId<UserData>): List<EventData> = dbQuery {
        EventEntity.find { EventsTable.idOrganizer eq id.toEntityId(UsersTable) }
            .map { it.toData() }
    }

    override suspend fun getForDates(startDate: LocalDateTime, endDate: LocalDateTime): List<EventData> = dbQuery {
        EventEntity.find { EventsTable.openingDateTime.between(startDate.toJavaLocalDateTime(), endDate.toJavaLocalDateTime())  }
            .map { it.toData() }
    }


    override suspend fun update(
        id: IxId<EventData>,
        organizerId: IxId<UserData>,
        eventCreateOrUpdateRequestData: EventData.EventCreateOrUpdateRequestData
    ): Boolean = dbQuery {
        val millisDifferenceFromOpening = eventCreateOrUpdateRequestData.openingDateTime.toInstant(TimeZone.UTC).toEpochMilliseconds() - DatetimeUtils.currentMillis()
        val shouldSetIsModified = millisDifferenceFromOpening > 0 && millisDifferenceFromOpening < (DatetimeUtils.oneDayMillis * 3)

        val updated = EventsTable.update({ userAndEventFilter(organizerId, id) }) {
            it[name] = eventCreateOrUpdateRequestData.name
            it[coverImageUrl] = eventCreateOrUpdateRequestData.coverImageUrl
            it[description] = eventCreateOrUpdateRequestData.description
            it[openingDateTime] = eventCreateOrUpdateRequestData.openingDateTime.toJavaLocalDateTime()
            it[doorOpeningDateTime] = eventCreateOrUpdateRequestData.doorOpeningDateTime.toJavaLocalDateTime()
            it[type] = eventCreateOrUpdateRequestData.type
            it[maxParticipants] = eventCreateOrUpdateRequestData.maxParticipants
            it[visibility] = eventCreateOrUpdateRequestData.visibility
            it[availability] = eventCreateOrUpdateRequestData.availability
            it[paymentLink] = eventCreateOrUpdateRequestData.paymentLink
            if (shouldSetIsModified) {
                it[isModified] = true
            }
        } > 0

        if (updated) {
            EventPlaceTable.deleteWhere { event eq id.toEntityId(EventsTable) }
            EventPlaceEntity.new {
                fromData(id, eventCreateOrUpdateRequestData.place)
            }
        }

        updated
    }

    override suspend fun delete(id: IxId<EventData>, organizerId: IxId<UserData>): Boolean = dbQuery {
        EventsTable.deleteWhere { userAndEventFilter(organizerId, id) } > 0
    }
}
package app.e_20.data.sources.db.schemas.event

import app.e_20.data.models.event.EventDto
import app.e_20.data.sources.db.schemas.user.UsersTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object EventTable : UUIDTable() {
    val name = varchar("event_name", 150)
    val coverImageUrl = varchar("cover_image_url", 200) // TODO
    val organizer = EventTable.reference(
        name = "id_organizer",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE // TODO
    )
    val description = varchar("description", 500)
    val place = EventTable.reference(
        name = "place",
        foreign = PlaceTable,
        onDelete = ReferenceOption.RESTRICT // TODO
    )
    val openingDateTime = datetime("opening_date_time")
    val doorOpeningDateTime = datetime("door_opening_date_time")
    val type = enumerationByName<EventDto.EventType>("event_type", 20)
    val maxParticipants = long("max_participants").nullable()
    val visibility = enumerationByName<EventDto.EventVisibility>("event_type", 20)
    val paymentLink = varchar("event_link", 200)
    val availability = enumerationByName<EventDto.EventAvailability>("availability", 20)
    val isModified = bool("is_modified")
    val timesShared = long("times_shared")
}

class EventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventEntity>(EventTable)

    val name by EventTable.name
    val coverImageUrl by EventTable.coverImageUrl
    val organizer by EventTable.organizer
    val description by EventTable.description
    val place by EventTable.place
    val openingDateTime by EventTable.openingDateTime
    val doorOpeningDateTime by EventTable.doorOpeningDateTime
    val type by EventTable.type
    val maxParticipants by EventTable.maxParticipants
    val visibility by EventTable.visibility
    val paymentLink by EventTable.paymentLink
    val availability by EventTable.availability
    val isModified by EventTable.isModified
    val timesShared by EventTable.timesShared
}
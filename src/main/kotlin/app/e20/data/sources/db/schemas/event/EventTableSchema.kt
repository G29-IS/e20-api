package app.e20.data.sources.db.schemas.event

import app.e20.data.models.event.EventData
import app.e20.data.sources.db.schemas.event.EventsTable.availability
import app.e20.data.sources.db.schemas.event.EventsTable.coverImageUrl
import app.e20.data.sources.db.schemas.event.EventsTable.description
import app.e20.data.sources.db.schemas.event.EventsTable.doorOpeningDateTime
import app.e20.data.sources.db.schemas.event.EventsTable.id
import app.e20.data.sources.db.schemas.event.EventsTable.idOrganizer
import app.e20.data.sources.db.schemas.event.EventsTable.isModified
import app.e20.data.sources.db.schemas.event.EventsTable.maxParticipants
import app.e20.data.sources.db.schemas.event.EventsTable.name
import app.e20.data.sources.db.schemas.event.EventsTable.openingDateTime
import app.e20.data.sources.db.schemas.event.EventsTable.paymentLink
import app.e20.data.sources.db.schemas.event.EventsTable.timesShared
import app.e20.data.sources.db.schemas.event.EventsTable.type
import app.e20.data.sources.db.schemas.event.EventsTable.visibility
import app.e20.data.sources.db.schemas.user.UsersTable
import app.e20.data.sources.db.toEntityId
import app.e20.data.sources.db.toIxId
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

/**
 * Named "Events" because "Event" is a reserved keyword
 *
 * @property id
 * @property name
 * @property coverImageUrl
 * @property idOrganizer
 * @property description
 * @property openingDateTime
 * @property doorOpeningDateTime
 * @property type
 * @property maxParticipants
 * @property visibility
 * @property availability
 * @property paymentLink
 * @property isModified
 * @property timesShared
 */
object EventsTable : UUIDTable() {
    val name = varchar("event_name", 150)
    val coverImageUrl = varchar("cover_image_url", 200) // TODO
    val idOrganizer = EventsTable.reference(
        name = "id_organizer",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE // TODO
    ).index()
    val description = varchar("description", 500)
    val openingDateTime = datetime("opening_date_time")
    val doorOpeningDateTime = datetime("door_opening_date_time")
    val type = enumerationByName<EventData.EventType>("event_type", 20)
    val maxParticipants = integer("max_participants").nullable()
    val visibility = enumerationByName<EventData.EventVisibility>("event_visibility", 20)
    val availability = enumerationByName<EventData.EventAvailability>("availability", 20)
    val paymentLink = varchar("event_link", 200).nullable()
    val isModified = bool("is_modified")
    val timesShared = long("times_shared")
}

/**
 * @property id
 * @property name
 * @property coverImageUrl
 * @property idOrganizer
 * @property description
 * @property place
 * @property openingDateTime
 * @property doorOpeningDateTime
 * @property type
 * @property maxParticipants
 * @property visibility
 * @property availability
 * @property paymentLink
 * @property isModified
 * @property timesShared
 */
class EventEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<EventEntity>(EventsTable)

    var name by EventsTable.name
    var coverImageUrl by EventsTable.coverImageUrl
    var idOrganizer by EventsTable.idOrganizer
    var description by EventsTable.description
    var openingDateTime by EventsTable.openingDateTime
    var doorOpeningDateTime by EventsTable.doorOpeningDateTime
    var type by EventsTable.type
    var maxParticipants by EventsTable.maxParticipants
    var visibility by EventsTable.visibility
    var paymentLink by EventsTable.paymentLink
    var availability by EventsTable.availability
    var isModified by EventsTable.isModified
    var timesShared by EventsTable.timesShared

    val place by EventPlaceEntity backReferencedOn EventPlaceTable.event
}

fun EventEntity.fromData(eventData: EventData) {
    name = eventData.name
    coverImageUrl = eventData.coverImageUrl
    idOrganizer = eventData.idOrganizer.toEntityId(UsersTable)
    description = eventData.description
    openingDateTime = eventData.openingDateTime.toJavaLocalDateTime()
    doorOpeningDateTime = eventData.doorOpeningDateTime.toJavaLocalDateTime()
    type = eventData.type
    maxParticipants = eventData.maxParticipants
    visibility = eventData.visibility
    paymentLink = eventData.paymentLink
    availability = eventData.availability
    isModified = eventData.isModified
    timesShared = eventData.timesShared
}

fun EventEntity.toData() = EventData(
    idEvent = id.toIxId(),
    name = name,
    coverImageUrl = coverImageUrl,
    idOrganizer = idOrganizer.toIxId(),
    description = description,
    place = place.toData(),
    openingDateTime = openingDateTime.toKotlinLocalDateTime(),
    doorOpeningDateTime = doorOpeningDateTime.toKotlinLocalDateTime(),
    type = type,
    maxParticipants = maxParticipants,
    visibility = visibility,
    availability = availability,
    paymentLink = paymentLink,
    isModified = isModified,
    timesShared = timesShared
)
package app.e20.data.sources.db.schemas.event

import app.e20.data.models.event.EventData
import app.e20.data.sources.db.schemas.event.EventTable.availability
import app.e20.data.sources.db.schemas.event.EventTable.coverImageUrl
import app.e20.data.sources.db.schemas.event.EventTable.description
import app.e20.data.sources.db.schemas.event.EventTable.doorOpeningDateTime
import app.e20.data.sources.db.schemas.event.EventTable.id
import app.e20.data.sources.db.schemas.event.EventTable.idOrganizer
import app.e20.data.sources.db.schemas.event.EventTable.isModified
import app.e20.data.sources.db.schemas.event.EventTable.maxParticipants
import app.e20.data.sources.db.schemas.event.EventTable.name
import app.e20.data.sources.db.schemas.event.EventTable.openingDateTime
import app.e20.data.sources.db.schemas.event.EventTable.paymentLink
import app.e20.data.sources.db.schemas.event.EventTable.timesShared
import app.e20.data.sources.db.schemas.event.EventTable.type
import app.e20.data.sources.db.schemas.event.EventTable.visibility
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
object EventTable : UUIDTable() {
    val name = varchar("event_name", 150)
    val coverImageUrl = varchar("cover_image_url", 200) // TODO
    val idOrganizer = EventTable.reference(
        name = "id_organizer",
        foreign = UsersTable,
        onDelete = ReferenceOption.CASCADE // TODO
    )
    val description = varchar("description", 500)
    val openingDateTime = datetime("opening_date_time")
    val doorOpeningDateTime = datetime("door_opening_date_time")
    val type = enumerationByName<EventData.EventType>("event_type", 20)
    val maxParticipants = integer("max_participants").nullable()
    val visibility = enumerationByName<EventData.EventVisibility>("event_type", 20)
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
    companion object : UUIDEntityClass<EventEntity>(EventTable)

    var name by EventTable.name
    var coverImageUrl by EventTable.coverImageUrl
    var idOrganizer by EventTable.idOrganizer
    var description by EventTable.description
    var openingDateTime by EventTable.openingDateTime
    var doorOpeningDateTime by EventTable.doorOpeningDateTime
    var type by EventTable.type
    var maxParticipants by EventTable.maxParticipants
    var visibility by EventTable.visibility
    var paymentLink by EventTable.paymentLink
    var availability by EventTable.availability
    var isModified by EventTable.isModified
    var timesShared by EventTable.timesShared

    val place by PlaceEntity backReferencedOn EventPlaceTable.event
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
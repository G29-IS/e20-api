package app.e20.data.sources.db.schemas.event

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import app.e20.data.models.event.EventPlaceData
import app.e20.data.sources.db.schemas.event.EventPlaceTable.address
import app.e20.data.sources.db.schemas.event.EventPlaceTable.id
import app.e20.data.sources.db.schemas.event.EventPlaceTable.name
import app.e20.data.sources.db.schemas.event.EventPlaceTable.url
import app.e20.data.sources.db.schemas.user.UsersTable.nullable
import app.e20.data.sources.db.toEntityId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

/**
 * @property id
 * @property name
 * @property address
 * @property url
 */
object EventPlaceTable: IntIdTable() {
    val event = reference(
        name = "id_event",
        foreign = EventsTable,
        onDelete = ReferenceOption.CASCADE,
    ).index()
    val name = varchar("place_name", 150).nullable()
    val address = varchar("address", 200)
    val url = varchar("url", 200).nullable()
}

/**
 * @property id
 * @property name
 * @property address
 * @property url
 */
class EventPlaceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventPlaceEntity>(EventPlaceTable)

    var event by EventPlaceTable.event
    var name by EventPlaceTable.name
    var address by EventPlaceTable.address
    var url by EventPlaceTable.url

    @Suppress("UNUSED")
    val eventEntity by EventEntity referencedOn EventPlaceTable.event
}

fun EventPlaceEntity.fromData(eventId: IxId<EventData>, eventPlaceData: EventPlaceData) {
    event = eventId.toEntityId(EventsTable)
    name = eventPlaceData.name
    address = eventPlaceData.address
    url = eventPlaceData.url
}

fun EventPlaceEntity.toData() = EventPlaceData(
    name = name,
    address = address,
    url = url
)
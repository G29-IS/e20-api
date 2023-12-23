package app.e20.data.sources.db.schemas.event

import app.e20.data.models.event.PlaceData
import app.e20.data.sources.db.schemas.event.EventPlaceTable.address
import app.e20.data.sources.db.schemas.event.EventPlaceTable.id
import app.e20.data.sources.db.schemas.event.EventPlaceTable.name
import app.e20.data.sources.db.schemas.event.EventPlaceTable.url
import app.e20.data.sources.db.toIxId
import app.e20.data.sources.db.toIxIntId
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import java.util.*

/**
 * @property id
 * @property name
 * @property address
 * @property url
 */
object EventPlaceTable: IntIdTable() {
    val event = reference(
        name = "id_event",
        foreign = EventTable,
        onDelete = ReferenceOption.CASCADE,
    ).index()
    val name = varchar("place_name", 150)
    val address = varchar("address", 200)
    val url = varchar("url", 200)
}

/**
 * @property id
 * @property name
 * @property address
 * @property url
 */
class PlaceEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PlaceEntity>(EventPlaceTable)

    var name by EventPlaceTable.name
    var address by EventPlaceTable.address
    var url by EventPlaceTable.url

    @Suppress("UNUSED")
    val eventEntity by EventEntity referencedOn EventPlaceTable.event
}

fun PlaceEntity.fromData(placeData: PlaceData) {
    name = placeData.name
    address = placeData.address
    url = placeData.url
}

fun PlaceEntity.toData() = PlaceData(
    name = name,
    address = address,
    url = url
)
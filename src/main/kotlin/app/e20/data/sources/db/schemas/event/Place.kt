package app.e20.data.sources.db.schemas.event

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

/*
-id_place: string
-name: string
-adr_address: string
-url: string
 */
object PlaceTable: UUIDTable() {
    val name = varchar("place_name", 150)
    val address = varchar("address", 200) // TODO: sotto proprietà
    val url = varchar("url", 200)
}

class PlaceEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<PlaceEntity>(PlaceTable)

    var name by PlaceTable.name
    var address by PlaceTable.address
    var url by PlaceTable.url
}
package app.e_20.data.sources.db

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.core.logic.typedId.impl.IxIntId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import java.util.*

fun <T> EntityID<UUID>.toIxId(): IxId<T> = IxId(value)
fun <T> EntityID<Int>.toIxIntId(): IxIntId<T> = IxIntId(value)


fun IxId<*>.toEntityId(table: IdTable<UUID>) = EntityID(this.id, table)
fun IxIntId<*>.toEntityIntId(table: IdTable<Int>) = EntityID(this.id, table)
package app.e20.data.sources.db

import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.impl.IxIntId
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import java.util.*

/**
 * Casts an [EntityID] of [UUID] to [IxId]
 */
fun <T> EntityID<UUID>.toIxId(): IxId<T> = IxId(value)

/**
 * Casts an [EntityID] of [Int] to [IxIntId]
 */
fun <T> EntityID<Int>.toIxIntId(): IxIntId<T> = IxIntId(value)


/**
 * Casts an [IxId] to an [EntityID] of [UUID]
 */
fun IxId<*>.toEntityId(table: IdTable<UUID>) = EntityID(this.id, table)

/**
 * Casts an [IxIntId] to an [EntityID] of [Int]
 */
fun IxIntId<*>.toEntityIntId(table: IdTable<Int>) = EntityID(this.id, table)
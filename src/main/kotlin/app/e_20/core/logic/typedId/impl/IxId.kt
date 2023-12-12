package app.e_20.core.logic.typedId.impl

import app.e_20.core.logic.typedId.Id
import java.util.*

/**
 * A [UUID] id.
 */
data class IxId<T>(val id: UUID) : Id<T> {
    
    constructor(id: String) : this(UUID.fromString(id))

    override fun toString(): String {
        return id.toString()
    }
}
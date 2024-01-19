package app.e20.core.logic.typedId.impl

import app.e20.core.logic.typedId.Id
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.io.Serial
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
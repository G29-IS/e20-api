package app.e20.core.logic.typedId.impl

import app.e20.core.logic.typedId.Id

/**
 * A [Int] id.
 */
data class IxIntId<T>(val id: Int) : Id<T> {

    constructor(id: String) : this(id.toInt())

    override fun toString(): String {
        return id.toString()
    }
}

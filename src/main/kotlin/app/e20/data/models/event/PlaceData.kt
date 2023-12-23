package app.e20.data.models.event

import app.e20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * @param name
 * @param address
 * @param url
 */
@Serializable
data class PlaceData(
    val name: String,
    val address: String,
    val url: String
)

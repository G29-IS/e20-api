package app.e20.data.models.event

import kotlinx.serialization.Serializable

/**
 * @param name
 * @param address
 * @param url
 */
@Serializable
data class EventPlaceData(
    val name: String?,
    val address: String,
    val url: String?
)

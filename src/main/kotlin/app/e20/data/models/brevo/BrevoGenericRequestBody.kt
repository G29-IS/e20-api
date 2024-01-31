package app.e20.data.models.brevo

import kotlinx.serialization.Serializable

@Serializable
data class BrevoGenericRequestBody(
    val to: List<To>,
    val templateId: Long,
) {
    @Serializable
    data class To (
        val email: String
    )
}
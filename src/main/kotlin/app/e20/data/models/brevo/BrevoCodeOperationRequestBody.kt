package app.e20.data.models.brevo

import kotlinx.serialization.Serializable

@Serializable
data class BrevoCodeOperationRequestBody(
    val to: List<BrevoGenericRequestBody.To>,
    val templateId: Long,
    val params: Params
) {
    @Serializable
    data class Params (
        val url: String
    )
}

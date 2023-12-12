package app.e_20.data.models.oauth.google

import kotlinx.serialization.Serializable

@Serializable
data class GoogleUserInfoDto(
    val email: String
)

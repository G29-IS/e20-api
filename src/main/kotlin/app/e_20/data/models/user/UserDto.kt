package app.e_20.data.models.user

import app.e_20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @Contextual val id: IxId<UserDto>,
    val email: String, // Received either via email registration, or google / apple oauth
    val passwordHash: String?, // Null when the account gets created with an oauth provider (google, apple...)
    // TODO: Actual values
) {

    enum class UserGender {
        MALE, FEMALE, NON_BINARY, OTHER
    }

    fun getResponseDto() = UserResponseDto(id, email)

    @Serializable
    data class UserResponseDto(
        @Contextual val id: IxId<UserDto>,
        val email: String
    )
}

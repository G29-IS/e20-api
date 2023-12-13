package app.e_20.data.models.user

import app.e_20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * @param id
 * @param email
 * @param passwordHash null when the account is created using oauth providers (google for this project)
 */
@Serializable
data class UserDto(
    @Contextual val id: IxId<UserDto>,
    val email: String,
    val passwordHash: String?,
    // TODO: Actual values
) {

    enum class UserGender {
        MALE, FEMALE, NON_BINARY, OTHER
    }
}

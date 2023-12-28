package app.e20.data.models.user

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.event.EventData
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * @param idUser
 * @param email
 * @param passwordHash null when the account is created using oauth providers (google for this project)
 * @param name
 * @param surname
 * @param username
 * @param phone
 * @param birthDate
 * @param gender
 * @param cityOfInterest
 * @param private true if account is private, false for public accounts
 * @param profileImageUrl
 */
@Serializable
data class UserData(
    @Contextual val idUser: IxId<UserData>,
    val email: String,
    val passwordHash: String?,
    val name: String,
    val surname: String,
    val username: String,
    val phone: String,
    val birthDate: LocalDate,
    val gender: UserGender,
    val cityOfInterest: String, // TODO
    val private: Boolean,
    val profileImageUrl: String
) {

    enum class UserGender {
        MALE, FEMALE, OTHER, NOT_SPECIFIED
    }

    @Serializable
    data class UserWithEventsOrganizedData(
        val user: UserData,
        val eventsOrganized: List<EventData>
    )
}

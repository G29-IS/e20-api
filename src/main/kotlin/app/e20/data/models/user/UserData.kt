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
    val cityOfInterest: String,
    val private: Boolean,
    val profileImageUrl: String
) {

    fun toUserPublicData() = UserPublicData(
        idUser = idUser,
        name = name,
        surname = surname,
        username = username,
        birthDate = birthDate,
        gender = gender,
        profileImageUrl = profileImageUrl
    )

    enum class UserGender {
        male, female, other, not_specified
    }

    @Serializable
    data class UserWithEventsOrganizedData(
        val user: UserPublicData,
        val eventsOrganized: List<EventData>
    )

    @Serializable
    data class UserPublicData(
        @Contextual val idUser: IxId<UserData>,
        val name: String,
        val surname: String,
        val username: String,
        val birthDate: LocalDate,
        val gender: UserGender,
        val profileImageUrl: String
    )
}

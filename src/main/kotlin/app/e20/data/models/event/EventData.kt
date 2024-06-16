package app.e20.data.models.event

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.UserData
import app.e20.data.validation.Validatable
import app.e20.data.validation.Validations
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


/**
 * @param idEvent
 * @param name
 * @param coverImageUrl
 * @param idOrganizer
 * @param description
 * @param place
 * @param openingDateTime
 * @param doorOpeningDateTime
 * @param type
 * @param maxParticipants
 * @param visibility
 * @param availability
 * @param paymentLink
 * @param isModified
 * @param timesShared
 */
@Serializable
data class EventData(
    @Contextual val idEvent: IxId<EventData>,
    val name: String,
    val coverImageUrl: String,
    @Contextual val idOrganizer: IxId<UserData>,
    val description: String,
    val place: EventPlaceData,
    val openingDateTime: LocalDateTime,
    val doorOpeningDateTime: LocalDateTime,
    val type: EventType,
    val maxParticipants: Int?,
    val visibility: EventVisibility,
    val availability: EventAvailability,
    val paymentLink: String?,
    val isModified: Boolean,
    val timesShared: Long
) {
    enum class EventType {
        CONCERT, FESTIVAL, BAR, CLUB, PARTY, HOUSEPARTY, PRIVATEPARTY, WORKSHOP, CONGRESS
    }

    enum class EventAvailability {
        AVAILABLE, RUNNING_OUT, SOLD_OUT, CANCELED
    }

    enum class EventVisibility {
        PUBLIC
    }

    @Serializable
    data class EventCreateOrUpdateRequestData(
        val name: String,
        val coverImageUrl: String,
        val description: String,
        val place: EventPlaceData,
        val openingDateTime: LocalDateTime,
        val doorOpeningDateTime: LocalDateTime,
        val type: EventType,
        val maxParticipants: Int? = null,
        val visibility: EventVisibility,
        val availability: EventAvailability,
        val paymentLink: String? = null
    ) : Validatable<EventCreateOrUpdateRequestData> {
        override fun validate() = Validation {
            EventCreateOrUpdateRequestData::name {
                minLength(Validations.Event.minNameLength)
                maxLength(Validations.Event.maxNameLength)
            }
            EventCreateOrUpdateRequestData::coverImageUrl {
                minLength(Validations.minImageUrlLength)
                maxLength(Validations.maxImageUrlLength)
            }
            EventCreateOrUpdateRequestData::description {
                minLength(Validations.Event.minDescriptionLength)
                maxLength(Validations.Event.maxDescriptionLength)
            }
            EventCreateOrUpdateRequestData::place {
                EventPlaceData::name ifPresent {
                    minLength(Validations.Event.minPlaceNameLength)
                    maxLength(Validations.Event.maxPlaceNameLength)
                }
                EventPlaceData::url ifPresent {
                    minLength(Validations.Event.minPlaceUrlLength)
                    maxLength(Validations.Event.maxPlaceUrlLength)
                }
                EventPlaceData::address {
                    minLength(Validations.Event.minPlaceAddressLength)
                    maxLength(Validations.Event.maxPlaceAddressLength)
                }
            }
            EventCreateOrUpdateRequestData::maxParticipants ifPresent {
                minimum(Validations.Event.minParticipants)
            }
            EventCreateOrUpdateRequestData::paymentLink ifPresent {
                minLength(Validations.Event.minPaymentLinkLength)
                maxLength(Validations.Event.maxPaymentLinkLength)
            }
        }.invoke(this)
    }
}
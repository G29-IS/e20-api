package app.e20.data.validation

object Validations {
    const val minImageUrlLength = 1
    const val maxImageUrlLength = 500

    object Event {
        const val minNameLength = 1
        const val maxNameLength = 200

        const val minDescriptionLength = 1
        const val maxDescriptionLength = 1000

        const val minPlaceNameLength = 1
        const val maxPlaceNameLength = 100

        const val minPlaceUrlLength = 1
        const val maxPlaceUrlLength = 500

        const val minPlaceAddressLength = 1
        const val maxPlaceAddressLength = 200

        const val minParticipants = 1

        const val minPaymentLinkLength = 1
        const val maxPaymentLinkLength = 500
    }
}
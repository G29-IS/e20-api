package app.e20.api.plugins

import app.e20.data.validation.Validatable
import app.e20.data.models.auth.PasswordResetRequestBody
import app.e20.data.models.event.EventData
import io.konform.validation.Valid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidator() {
    install(RequestValidation) {
        validateValidatable<PasswordResetRequestBody>()
        validateValidatable<EventData.EventCreateOrUpdateRequestData>()
    }
}

inline fun <reified T : Validatable<T>> RequestValidationConfig.validateValidatable() =
    validate<T> {
        val validationResult = it.validate()
        if (validationResult is Valid)
            ValidationResult.Valid
        else
            ValidationResult.Invalid(
                validationResult.errors.map { error ->  "${error.dataPath}: ${error.message}" }
            )
    }

package app.e_20.api.plugins

import app.e_20.data.models.Validatable
import app.e_20.data.models.auth.PasswordResetRequestBody
import io.konform.validation.Valid
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidator() {
    install(RequestValidation) {
        validateValidatable<PasswordResetRequestBody>()
        // TODO: Other validatables
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

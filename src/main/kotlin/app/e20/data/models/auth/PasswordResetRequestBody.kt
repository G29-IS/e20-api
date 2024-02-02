package app.e20.data.models.auth

import app.e20.core.logic.RegexPatterns.emailPattern
import app.e20.data.validation.Validatable
import app.e20.data.validation.Validations
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class PasswordResetRequestBody(
    val password: String
): Validatable<PasswordResetRequestBody> {
    override fun validate(): ValidationResult<PasswordResetRequestBody> =
        Validation {
            PasswordResetRequestBody::password {
                minLength(8) hint "Password min length is 8 characters"
                maxLength(100) hint "Password max length is 100 characters"
            }
        }.invoke(this)
}


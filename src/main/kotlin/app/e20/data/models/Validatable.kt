package app.e20.data.models

import io.konform.validation.ValidationResult

/**
 * Validatable data of type [T]
 */
interface Validatable<T> {
    fun validate(): ValidationResult<T>
}

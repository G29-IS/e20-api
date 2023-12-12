package app.e_20.data.models.user

import app.e_20.core.logic.DatetimeUtils
import app.e_20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


/**
 * @param token should be randomly generated and hashed
 */
@Serializable
data class PasswordResetDto(
    val token: String,
    @Contextual val userId: IxId<UserDto>,
    @Contextual val expireAt: Long,
    @Contextual val createdAt: Long = DatetimeUtils.currentMillis()
)
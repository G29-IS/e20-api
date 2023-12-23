package app.e20.data.models.user

import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.typedId.impl.IxId
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


/**
 * @param token should be randomly generated and hashed
 */
@Serializable
data class PasswordResetData(
    val token: String,
    @Contextual val userId: IxId<UserData>,
    @Contextual val expireAt: Long,
    @Contextual val createdAt: Long = DatetimeUtils.currentMillis()
)
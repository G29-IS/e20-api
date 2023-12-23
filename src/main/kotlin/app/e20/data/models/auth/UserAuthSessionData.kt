package app.e20.data.models.auth

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.UserData
import io.ktor.server.auth.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/**
 * @param id session id
 * @param userId
 * @param iat timestamp of when the session was issues (iat = issued at)
 * @param deviceName device from which the session was requested
 * @param ip internet ip from which the session was requested
 */
@Serializable
data class UserAuthSessionData(
    @Contextual val id: IxId<UserAuthSessionData>,
    @Contextual val userId: IxId<UserData>,
    val iat: Long,
    val deviceName: String?,
    val ip: String
) : Principal

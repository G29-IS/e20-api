package app.e_20.data.models.auth

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.user.UserDto
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
data class UserAuthSessionDto(
    @Contextual val id: IxId<UserAuthSessionDto>,
    @Contextual val userId: IxId<UserDto>,
    val iat: Long,
    val deviceName: String?,
    val ip: String
) : Principal

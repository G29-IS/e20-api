package app.e_20.data.sources.cache.cm

import app.e_20.config.ApiConfig
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.auth.UserAuthSessionDto
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.cache.cm.UserSessionCM.cache
import app.e_20.data.sources.cache.cm.UserSessionCM.delete
import app.e_20.data.sources.cache.cm.UserSessionCM.deleteAll
import app.e_20.data.sources.cache.cm.UserSessionCM.get
import app.e_20.data.sources.cache.core.ExpiringCM

/**
 * User auth session storage in cache
 *
 * @see get
 * @see cache
 * @see delete
 * @see deleteAll
 */
object UserSessionCM : ExpiringCM("sessions", (ApiConfig.sessionMaxAgeInSeconds + 10)) {
    private fun keyValue(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = "${userId}:$sessionId"

    fun get(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) : UserAuthSessionDto? = get(
        keyValue(
            userId,
            sessionId
        )
    )

    fun cache(userAuthSessionDto: UserAuthSessionDto) =
        cache(keyValue(userAuthSessionDto.userId, userAuthSessionDto.id), userAuthSessionDto)

    fun delete(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = delete(
        keyValue(
            userId,
            sessionId
        )
    )

    /**
     * Deletes all sessions of the user with the given [userId]
     */
    fun deleteAll(userId: IxId<UserDto>) = delete("${userId}:*")
}

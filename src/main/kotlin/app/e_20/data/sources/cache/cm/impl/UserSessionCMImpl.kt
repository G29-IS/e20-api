package app.e_20.data.sources.cache.cm.impl

import app.e_20.config.ApiConfig
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.auth.UserAuthSessionDto
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.cache.cm.UserSessionCM
import app.e_20.data.sources.cache.cm.impl.UserSessionCMImpl.cache
import app.e_20.data.sources.cache.cm.impl.UserSessionCMImpl.delete
import app.e_20.data.sources.cache.cm.impl.UserSessionCMImpl.deleteAll
import app.e_20.data.sources.cache.cm.impl.UserSessionCMImpl.get
import app.e_20.data.sources.cache.core.ExpiringCM

object UserSessionCMImpl : UserSessionCM, ExpiringCM("sessions", (ApiConfig.sessionMaxAgeInSeconds + 10)) {
    private fun keyValue(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = "${userId}:$sessionId"

    override fun get(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) : UserAuthSessionDto? = get(
        keyValue(
            userId,
            sessionId
        )
    )

    override fun cache(userAuthSessionDto: UserAuthSessionDto) =
        cache(keyValue(userAuthSessionDto.userId, userAuthSessionDto.id), userAuthSessionDto)

    override fun delete(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = delete(
        keyValue(
            userId,
            sessionId
        )
    )

    override fun deleteAll(userId: IxId<UserDto>) = delete("${userId}:*")
}

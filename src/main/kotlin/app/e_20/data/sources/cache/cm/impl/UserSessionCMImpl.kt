package app.e_20.data.sources.cache.cm.impl

import app.e_20.config.ApiConfig
import app.e_20.core.logic.ObjectMapper
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.auth.UserAuthSessionDto
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.cache.RedisClient
import app.e_20.data.sources.cache.cm.UserSessionCM
import app.e_20.data.sources.cache.core.ExpiringCM

class UserSessionCMImpl(
    redisClient: RedisClient,
    objectMapper: ObjectMapper
) : UserSessionCM,
    ExpiringCM(
        keyBase = "sessions",
        expirationInSeconds = (ApiConfig.sessionMaxAgeInSeconds + 10),
        redisClient,
        objectMapper
    )
{
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

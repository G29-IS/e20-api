package app.e20.data.sources.cache.cm.impl

import app.e20.config.ApiConfig
import app.e20.core.clients.RedisClient
import app.e20.core.logic.ObjectMapper
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.auth.UserAuthSessionData
import app.e20.data.models.user.UserData
import app.e20.data.sources.cache.cm.UserSessionCM
import app.e20.data.sources.cache.core.ExpiringCM
import org.koin.core.annotation.Single

@Single(createdAtStart = true, binds = [UserSessionCM::class])
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
    private fun keyValue(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>) = "${userId}:$sessionId"

    override fun get(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>) : UserAuthSessionData? = get(
        keyValue(
            userId,
            sessionId
        )
    )

    override fun cache(userAuthSessionData: UserAuthSessionData) =
        cache(keyValue(userAuthSessionData.userId, userAuthSessionData.id), userAuthSessionData)

    override fun delete(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>) = delete(
        keyValue(
            userId,
            sessionId
        )
    )

    override fun deleteAll(userId: IxId<UserData>) = delete("${userId}:*")
}

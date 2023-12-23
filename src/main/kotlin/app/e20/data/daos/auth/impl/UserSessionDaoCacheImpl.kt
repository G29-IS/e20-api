package app.e20.data.daos.auth.impl

import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.newIxId
import app.e20.data.daos.auth.UserSessionDao
import app.e20.data.models.auth.UserAuthSessionDto
import app.e20.data.models.user.UserDto
import app.e20.data.sources.cache.cm.UserSessionCM
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserSessionDaoCacheImpl(
    private val userSessionCM: UserSessionCM
) : UserSessionDao {
    override fun get(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = userSessionCM.get(userId, sessionId)

    override fun create(userId: IxId<UserDto>, device: String?, ip: String): IxId<UserAuthSessionDto> {
        val sessionId = newIxId<UserAuthSessionDto>()

        save(
            UserAuthSessionDto(
                id = sessionId,
                userId = userId,
                iat = DatetimeUtils.currentMillis(),
                deviceName = device,
                ip = ip
            )
        )

        return sessionId
    }

    private fun save(userAuthSessionDto: UserAuthSessionDto) = userSessionCM.cache(userAuthSessionDto)

    override fun delete(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) =
        userSessionCM.delete(userId, sessionId)

    override fun deleteAllSessionsOfUser(userId: IxId<UserDto>) = userSessionCM.deleteAll(userId)
}

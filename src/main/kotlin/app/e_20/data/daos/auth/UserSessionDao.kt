package app.e_20.data.daos.auth

import app.e_20.core.logic.DatetimeUtils
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.core.logic.typedId.newIxId
import app.e_20.data.models.auth.UserAuthSessionDto
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.cache.cm.UserSessionCM

object UserSessionDao {
    fun get(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = UserSessionCM.get(userId, sessionId)

    fun create(userId: IxId<UserDto>, device: String?, ip: String): IxId<UserAuthSessionDto> {
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

    private fun save(userAuthSessionDto: UserAuthSessionDto) = UserSessionCM.cache(userAuthSessionDto)

    fun delete(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>) = UserSessionCM.delete(userId, sessionId)

    fun deleteAllSessionsOfUser(userId: IxId<UserDto>) = UserSessionCM.deleteAll(userId)
}

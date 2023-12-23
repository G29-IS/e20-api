package app.e20.data.daos.auth.impl

import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.newIxId
import app.e20.data.daos.auth.UserSessionDao
import app.e20.data.models.auth.UserAuthSessionData
import app.e20.data.models.user.UserData
import app.e20.data.sources.cache.cm.UserSessionCM
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserSessionDaoCacheImpl(
    private val userSessionCM: UserSessionCM
) : UserSessionDao {
    override fun get(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>) = userSessionCM.get(userId, sessionId)

    override fun create(userId: IxId<UserData>, device: String?, ip: String): IxId<UserAuthSessionData> {
        val sessionId = newIxId<UserAuthSessionData>()

        save(
            UserAuthSessionData(
                id = sessionId,
                userId = userId,
                iat = DatetimeUtils.currentMillis(),
                deviceName = device,
                ip = ip
            )
        )

        return sessionId
    }

    private fun save(userAuthSessionData: UserAuthSessionData) = userSessionCM.cache(userAuthSessionData)

    override fun delete(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>) =
        userSessionCM.delete(userId, sessionId)

    override fun deleteAllSessionsOfUser(userId: IxId<UserData>) = userSessionCM.deleteAll(userId)
}

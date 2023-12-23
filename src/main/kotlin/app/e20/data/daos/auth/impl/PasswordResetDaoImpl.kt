package app.e20.data.daos.auth.impl

import app.e20.core.clients.BrevoClient
import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.TokenGenerator
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.daos.auth.PasswordResetDao
import app.e20.data.models.user.PasswordResetData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.user.PasswordResetDBI
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class PasswordResetDaoImpl(
    private val passwordResetDBI: PasswordResetDBI,
    private val tokenGenerator: TokenGenerator,
    private val brevoClient: BrevoClient
) : PasswordResetDao {
    private suspend fun save(passwordResetData: PasswordResetData) =
        passwordResetDBI.save(passwordResetData)

    override suspend fun get(token: String): PasswordResetData? =
        passwordResetDBI.get(token)

    override suspend fun createAndSend(user: UserData): Boolean {
        val (token, hashedToken) = tokenGenerator.generate()

        val passwordResetData = PasswordResetData(
            token = hashedToken,
            userId = user.id,
            expireAt = DatetimeUtils.currentMillis() + 3600000
        )

        save(passwordResetData)
        return brevoClient.sendPasswordResetEmail(user.email, token)
    }

    override suspend fun isRateLimited(id: IxId<UserData>): Boolean {
        val sent = passwordResetDBI.count(id)
        return sent >= 7
    }
}

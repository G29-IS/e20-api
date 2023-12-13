package app.e_20.data.daos.auth.impl

import app.e_20.core.clients.BrevoClient
import app.e_20.core.logic.DatetimeUtils
import app.e_20.core.logic.TokenGenerator
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.daos.auth.PasswordResetDao
import app.e_20.data.models.user.PasswordResetDto
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.dbi.user.PasswordResetDBI
import app.e_20.data.sources.db.dbi.user.impl.PasswordResetDBIImpl

object PasswordResetDaoImpl: PasswordResetDao {
    private val passwordResetDBI: PasswordResetDBI = PasswordResetDBIImpl

    private suspend fun save(passwordResetDto: PasswordResetDto) =
        passwordResetDBI.save(passwordResetDto)

    override suspend fun get(token: String): PasswordResetDto? =
        passwordResetDBI.get(token)

    override suspend fun createAndSend(user: UserDto): Boolean {
        val (token, hashedToken) = TokenGenerator.generate()

        val passwordResetDto = PasswordResetDto(
            token = hashedToken,
            userId = user.id,
            expireAt = DatetimeUtils.currentMillis() + 3600000
        )

        save(passwordResetDto)
        return BrevoClient.sendPasswordResetEmail(user.email, token)
    }

    override suspend fun isRateLimited(id: IxId<UserDto>): Boolean {
        val sent = passwordResetDBI.count(id)
        return sent >= 7
    }
}

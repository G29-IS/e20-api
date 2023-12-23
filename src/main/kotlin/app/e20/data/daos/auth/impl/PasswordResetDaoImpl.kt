package app.e20.data.daos.auth.impl

import app.e20.core.clients.BrevoClient
import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.TokenGenerator
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.daos.auth.PasswordResetDao
import app.e20.data.models.user.PasswordResetDto
import app.e20.data.models.user.UserDto
import app.e20.data.sources.db.dbi.user.PasswordResetDBI
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class PasswordResetDaoImpl(
    private val passwordResetDBI: PasswordResetDBI,
    private val tokenGenerator: TokenGenerator,
    private val brevoClient: BrevoClient
) : PasswordResetDao {
    private suspend fun save(passwordResetDto: PasswordResetDto) =
        passwordResetDBI.save(passwordResetDto)

    override suspend fun get(token: String): PasswordResetDto? =
        passwordResetDBI.get(token)

    override suspend fun createAndSend(user: UserDto): Boolean {
        val (token, hashedToken) = tokenGenerator.generate()

        val passwordResetDto = PasswordResetDto(
            token = hashedToken,
            userId = user.id,
            expireAt = DatetimeUtils.currentMillis() + 3600000
        )

        save(passwordResetDto)
        return brevoClient.sendPasswordResetEmail(user.email, token)
    }

    override suspend fun isRateLimited(id: IxId<UserDto>): Boolean {
        val sent = passwordResetDBI.count(id)
        return sent >= 7
    }
}

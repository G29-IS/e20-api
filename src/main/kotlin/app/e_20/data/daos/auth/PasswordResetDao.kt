package app.e_20.data.daos.auth

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.user.PasswordResetDto
import app.e_20.data.models.user.UserDto

/**
 * [PasswordResetDto] data access object (DAO)
 *
 * @see get
 * @see createAndSend
 * @see isRateLimited
 */
interface PasswordResetDao {
    /**
     * Gets the password reset data from the given token
     */
    suspend fun get(token: String): PasswordResetDto?

    /**
     * Create a password reset token and sends an email with reset instructions to the provided [user]
     *
     * @return true if the email was sent successfully, false otherwise
     */
    suspend fun createAndSend(user: UserDto): Boolean

    /**
     * Whether resetting the password is rate limited for the provided user [id]
     *
     * @return true if rate limited, false otherwise
     */
    suspend fun isRateLimited(id: IxId<UserDto>): Boolean
}
package app.e20.data.daos.auth

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.PasswordResetData
import app.e20.data.models.user.UserData

/**
 * [PasswordResetData] data access object (DAO)
 *
 * @see get
 * @see createAndSend
 * @see isRateLimited
 */
interface PasswordResetDao {
    /**
     * Gets the password reset data from the given token
     */
    suspend fun get(token: String): PasswordResetData?

    /**
     * Create a password reset token and sends an email with reset instructions to the provided [user]
     *
     * @return true if the email was sent successfully, false otherwise
     */
    suspend fun createAndSend(user: UserData): Boolean

    /**
     * Whether resetting the password is rate limited for the provided user [id]
     *
     * @return true if rate limited, false otherwise
     */
    suspend fun isRateLimited(id: IxId<UserData>): Boolean
}
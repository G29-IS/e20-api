package app.e20.data.daos.user

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.UserData

/**
 * [UserData] data access object (DAO)
 *
 * @see create
 * @see get
 * @see getFromEmail
 * @see resetPassword
 * @see delete
 */
interface UserDao {

    /**
     * Creates a new user
     */
    suspend fun create(userData: UserData)

    /**
     * Finds a user by the given [id]
     */
    suspend fun get(id: IxId<UserData>) : UserData?

    /**
     * Finds a user by the given [email]
     *
     * **This method should only be used in the login process**
     */
    suspend fun getFromEmail(email: String) : UserData?

    /**
     * Resets the user password with a [newPasswordHashed]
     */
    suspend fun resetPassword(id: IxId<UserData>, newPasswordHashed: String)

    /**
     * Deletes the user with the given [id]
     */
    suspend fun delete(id: IxId<UserData>)
}
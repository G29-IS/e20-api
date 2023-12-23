package app.e20.data.sources.db.dbi.user

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.DBI

/**
 * [UserData] database interactor
 *
 * @see create
 * @see get
 * @see get
 * @see resetPassword
 * @see delete
 */
interface UserDBI : DBI {
    /**
     * Creates a new [UserData] entity in the database
     */
    suspend fun create(userData: UserData)

    /**
     * Gets a [UserData] entity from the database
     */
    suspend fun get(id: IxId<UserData>): UserData?

    /**
     * Gets a [UserData] entity from the database querying by the [email]
     */
    suspend fun get(email: String): UserData?

    /**
     * Sets the [newPasswordHashed] for the user with the given [id]
     */
    suspend fun resetPassword(id: IxId<UserData>, newPasswordHashed: String)

    /**
     * Deletes a user entity with the given [id]
     */
    suspend fun delete(id: IxId<UserData>)
}
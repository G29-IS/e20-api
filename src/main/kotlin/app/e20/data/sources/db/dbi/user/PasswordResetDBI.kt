package app.e20.data.sources.db.dbi.user

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.PasswordResetData
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.DBI

/**
 * [PasswordResetData] database interactor
 *
 * @see count
 * @see save
 * @see get
 * @see deleteAll
 */
interface PasswordResetDBI : DBI {
    /**
     * Counts the amount of password reset entities in the database
     */
    suspend fun count(id: IxId<UserData>): Long

    /**
     * Saves a [passwordResetData] in the database
     */
    suspend fun save(passwordResetData: PasswordResetData)

    /**
     * Gets a [PasswordResetData] from the given [token]
     */
    suspend fun get(token: String): PasswordResetData?

    /**
     * Deletes all [PasswordResetData] of a user with the given [id]
     */
    suspend fun deleteAll(id: IxId<UserData>)
}

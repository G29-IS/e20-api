package app.e20.data.sources.db.dbi.user

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.PasswordResetDto
import app.e20.data.models.user.UserDto
import app.e20.data.sources.db.dbi.DBI

/**
 * [PasswordResetDto] database interactor
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
    suspend fun count(id: IxId<UserDto>): Long

    /**
     * Saves a [passwordResetDto] in the database
     */
    suspend fun save(passwordResetDto: PasswordResetDto)

    /**
     * Gets a [PasswordResetDto] from the given [token]
     */
    suspend fun get(token: String): PasswordResetDto?

    /**
     * Deletes all [PasswordResetDto] of a user with the given [id]
     */
    suspend fun deleteAll(id: IxId<UserDto>)
}

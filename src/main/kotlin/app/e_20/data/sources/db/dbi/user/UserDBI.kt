package app.e_20.data.sources.db.dbi.user

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.dbi.DBI

/**
 * [UserDto] database interactor
 *
 * @see create
 * @see get
 * @see get
 * @see resetPassword
 * @see delete
 */
interface UserDBI : DBI {
    /**
     * Creates a new [UserDto] entity in the database
     */
    suspend fun create(userDto: UserDto)

    /**
     * Gets a [UserDto] entity from the database
     */
    suspend fun get(id: IxId<UserDto>): UserDto?

    /**
     * Gets a [UserDto] entity from the database querying by the [email]
     */
    suspend fun get(email: String): UserDto?

    /**
     * Sets the [newPasswordHashed] for the user with the given [id]
     */
    suspend fun resetPassword(id: IxId<UserDto>, newPasswordHashed: String)

    /**
     * Deletes a user entity with the given [id]
     */
    suspend fun delete(id: IxId<UserDto>)
}
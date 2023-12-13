package app.e_20.data.daos.auth

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.auth.UserAuthSessionDto
import app.e_20.data.models.user.UserDto

/**
 * [UserAuthSessionDto] data access object (DAO)
 *
 * @see get
 * @see create
 * @see delete
 * @see deleteAllSessionsOfUser
 */
interface UserSessionDao {
    /**
     * Gets the [UserAuthSessionDto] for the given [sessionId] of [userId]
     *
     * @return [UserAuthSessionDto] or null if not session was found
     */
    fun get(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>): UserAuthSessionDto?

    /**
     * Creates a new session for the [userId]
     *
     * @param userId
     * @param device device for which the session was requested
     * @param ip ip of the device
     *
     * @return [IxId] of the [UserAuthSessionDto]
     */
    fun create(userId: IxId<UserDto>, device: String?, ip: String): IxId<UserAuthSessionDto>

    /**
     * Deletes a user auth session
     */
    fun delete(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>)

    /**
     * Deletes all existing auth session of a given [userId]
     */
    fun deleteAllSessionsOfUser(userId: IxId<UserDto>)
}
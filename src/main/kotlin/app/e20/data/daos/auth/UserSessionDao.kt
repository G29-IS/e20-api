package app.e20.data.daos.auth

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.auth.UserAuthSessionData
import app.e20.data.models.user.UserData

/**
 * [UserAuthSessionData] data access object (DAO)
 *
 * @see get
 * @see create
 * @see delete
 * @see deleteAllSessionsOfUser
 */
interface UserSessionDao {
    /**
     * Gets the [UserAuthSessionData] for the given [sessionId] of [userId]
     *
     * @return [UserAuthSessionData] or null if not session was found
     */
    fun get(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>): UserAuthSessionData?

    /**
     * Creates a new session for the [userId]
     *
     * @param userId
     * @param device device for which the session was requested
     * @param ip ip of the device
     *
     * @return [IxId] of the [UserAuthSessionData]
     */
    fun create(userId: IxId<UserData>, device: String?, ip: String): IxId<UserAuthSessionData>

    /**
     * Deletes a user auth session
     */
    fun delete(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>)

    /**
     * Deletes all existing auth session of a given [userId]
     */
    fun deleteAllSessionsOfUser(userId: IxId<UserData>)
}
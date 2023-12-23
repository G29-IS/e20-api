package app.e20.data.sources.cache.cm

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.auth.UserAuthSessionData
import app.e20.data.models.user.UserData

/**
 * User auth session storage in cache
 *
 * @see get
 * @see cache
 * @see delete
 * @see deleteAll
 */
interface UserSessionCM {
    /**
     * Gets a session from the cache by the [sessionId] and [userId]
     */
    fun get(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>): UserAuthSessionData?

    /**
     * Stores a [userAuthSessionData] in the cache
     */
    fun cache(userAuthSessionData: UserAuthSessionData)

    /**
     * Deletes a session from the cache by the [userId] and [sessionId]
     */
    fun delete(userId: IxId<UserData>, sessionId: IxId<UserAuthSessionData>)


    /**
     * Deletes all sessions of the user with the given [userId] from the cache
     */
    fun deleteAll(userId: IxId<UserData>)
}
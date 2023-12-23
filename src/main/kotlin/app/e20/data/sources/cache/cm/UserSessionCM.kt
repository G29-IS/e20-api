package app.e20.data.sources.cache.cm

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.auth.UserAuthSessionDto
import app.e20.data.models.user.UserDto

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
    fun get(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>): UserAuthSessionDto?

    /**
     * Stores a [userAuthSessionDto] in the cache
     */
    fun cache(userAuthSessionDto: UserAuthSessionDto)

    /**
     * Deletes a session from the cache by the [userId] and [sessionId]
     */
    fun delete(userId: IxId<UserDto>, sessionId: IxId<UserAuthSessionDto>)


    /**
     * Deletes all sessions of the user with the given [userId] from the cache
     */
    fun deleteAll(userId: IxId<UserDto>)
}
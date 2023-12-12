package app.e_20.data.daos.user

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.dbi.user.impl.UserDBIImpl

// TODO: Docs
object UserDao {

    suspend fun create(userDto: UserDto) {
        UserDBIImpl.create(userDto)
    }

    suspend fun get(id: IxId<UserDto>) : UserDto? {
        return UserDBIImpl.get(id)
    }

    /**
     * **This method should be only used in the login route**
     */
    suspend fun getFromEmail(email: String) : UserDto? {
        return UserDBIImpl.get(email)
    }

    suspend fun resetPassword(id: IxId<UserDto>, newPasswordHashed: String) {
        UserDBIImpl.resetPassword(id, newPasswordHashed)
    }

    suspend fun delete(id: IxId<UserDto>) {
        UserDBIImpl.delete(id)
    }
}

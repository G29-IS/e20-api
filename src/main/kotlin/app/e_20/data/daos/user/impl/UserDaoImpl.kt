package app.e_20.data.daos.user.impl

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.daos.user.UserDao
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.dbi.user.UserDBI
import app.e_20.data.sources.db.dbi.user.impl.UserDBIImpl
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserDaoImpl(
    private val userDBI: UserDBI
) : UserDao {

    override suspend fun create(userDto: UserDto) {
        userDBI.create(userDto)
    }

    override suspend fun get(id: IxId<UserDto>) : UserDto? {
        return userDBI.get(id)
    }

    override suspend fun getFromEmail(email: String) : UserDto? {
        return userDBI.get(email)
    }

    override suspend fun resetPassword(id: IxId<UserDto>, newPasswordHashed: String) {
        userDBI.resetPassword(id, newPasswordHashed)
    }

    override suspend fun delete(id: IxId<UserDto>) {
        userDBI.delete(id)
    }
}

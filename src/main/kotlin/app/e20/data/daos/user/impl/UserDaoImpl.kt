package app.e20.data.daos.user.impl

import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.daos.user.UserDao
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.dbi.user.UserDBI
import org.koin.core.annotation.Single

@Single(createdAtStart = true)
class UserDaoImpl(
    private val userDBI: UserDBI
) : UserDao {

    override suspend fun create(userData: UserData) {
        userDBI.create(userData)
    }

    override suspend fun get(id: IxId<UserData>) : UserData? {
        return userDBI.get(id)
    }

    override suspend fun getFromEmail(email: String) : UserData? {
        return userDBI.get(email)
    }

    override suspend fun resetPassword(id: IxId<UserData>, newPasswordHashed: String) {
        userDBI.resetPassword(id, newPasswordHashed)
    }

    override suspend fun delete(id: IxId<UserData>) {
        userDBI.delete(id)
    }
}

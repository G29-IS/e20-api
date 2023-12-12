package app.e_20.data.sources.db.dbi.user

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.dbi.DBI

interface UserDBI : DBI {
    suspend fun create(userDto: UserDto)
    suspend fun get(id: IxId<UserDto>): UserDto?
    suspend fun get(email: String): UserDto?
    suspend fun resetPassword(id: IxId<UserDto>, newPasswordHashed: String)
    suspend fun delete(id: IxId<UserDto>)
}
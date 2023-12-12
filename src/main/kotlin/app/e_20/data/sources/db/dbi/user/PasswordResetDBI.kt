package app.e_20.data.sources.db.dbi.user

import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.data.models.user.PasswordResetDto
import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.dbi.DBI

interface PasswordResetDBI : DBI {
    suspend fun count(id: IxId<UserDto>): Long
    suspend fun save(passwordResetDto: PasswordResetDto)
    suspend fun get(token: String): PasswordResetDto?
    suspend fun deleteAll(id: IxId<UserDto>)
}

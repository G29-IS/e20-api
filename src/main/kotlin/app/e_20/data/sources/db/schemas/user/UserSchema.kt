package app.e_20.data.sources.db.schemas.user

import app.e_20.data.sources.db.schemas.user.UsersTable.email
import app.e_20.data.sources.db.schemas.user.UsersTable.id
import app.e_20.data.sources.db.schemas.user.UsersTable.passwordHash
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

/**
 * users instead of "user" because the latter is a reserved keyword in postgres
 *
 * @property id
 * @property email
 * @property passwordHash
 */
object UsersTable : UUIDTable() {
    val email = varchar("email", 150).uniqueIndex()
    val passwordHash = varchar("password_hash", 100).nullable()
}

/**
 * @property id
 * @property email
 * @property passwordHash
 */
class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
}
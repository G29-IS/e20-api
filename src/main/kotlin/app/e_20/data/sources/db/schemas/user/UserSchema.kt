package app.e_20.data.sources.db.schemas.user

import app.e_20.data.models.user.UserDto
import app.e_20.data.sources.db.schemas.user.UsersTable.email
import app.e_20.data.sources.db.schemas.user.UsersTable.id
import app.e_20.data.sources.db.schemas.user.UsersTable.passwordHash
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import java.util.*

/**
 * users instead of "user" because the latter is a reserved keyword in postgres
 *
 * TODO: Docs
 * @property id
 * @property email
 * @property passwordHash
 */
object UsersTable : UUIDTable() {
    val email = varchar("email", 150).uniqueIndex()
    val passwordHash = varchar("password_hash", 100).nullable()
    val name = varchar("full_name", 150)
    val surname = varchar("surname", 150)
    val username = varchar("username", 100)
    val phone = varchar("phone", 20)
    val birthDate = date("birth_date")
    val gender = enumerationByName<UserDto.UserGender>("gender", 20)
    val cityOfInterest = varchar("city_of_interest", 150) // TODO
    val private = bool("is_private")
    val profileImage = varchar("profile_image", 200) // TODO: Check length
}

/**
 * TODO: Docs
 */
class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    val name by UsersTable.name
    val surname by UsersTable.surname
    val username by UsersTable.username
    val phone by UsersTable.phone
    val birthDate by UsersTable.birthDate
    val gender by UsersTable.gender
    val cityOfInterest by UsersTable.cityOfInterest
    val private by UsersTable.private
    val profileImage by UsersTable.profileImage
}
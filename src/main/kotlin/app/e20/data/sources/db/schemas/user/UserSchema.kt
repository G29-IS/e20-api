package app.e20.data.sources.db.schemas.user

import app.e20.data.models.user.UserData
import app.e20.data.sources.db.schemas.user.UsersTable.birthDate
import app.e20.data.sources.db.schemas.user.UsersTable.cityOfInterest
import app.e20.data.sources.db.schemas.user.UsersTable.email
import app.e20.data.sources.db.schemas.user.UsersTable.gender
import app.e20.data.sources.db.schemas.user.UsersTable.id
import app.e20.data.sources.db.schemas.user.UsersTable.name
import app.e20.data.sources.db.schemas.user.UsersTable.passwordHash
import app.e20.data.sources.db.schemas.user.UsersTable.phone
import app.e20.data.sources.db.schemas.user.UsersTable.private
import app.e20.data.sources.db.schemas.user.UsersTable.profileImageUrl
import app.e20.data.sources.db.schemas.user.UsersTable.surname
import app.e20.data.sources.db.schemas.user.UsersTable.username
import app.e20.data.sources.db.toIxId
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import java.util.*

/**
 * users instead of "user" because the latter is a reserved keyword in postgres
 *
 * @property id
 * @property email
 * @property passwordHash null when the account is created using oauth providers (google for this project)
 * @property name
 * @property surname
 * @property username
 * @property phone
 * @property birthDate
 * @property gender
 * @property cityOfInterest
 * @property private true if account is private, false for public accounts
 * @property profileImageUrl
 */
object UsersTable : UUIDTable() {
    val email = varchar("email", 150).uniqueIndex()
    val passwordHash = varchar("password_hash", 100).nullable()
    val name = varchar("full_name", 150)
    val surname = varchar("surname", 150)
    val username = varchar("username", 100)
    val phone = varchar("phone", 20)
    val birthDate = date("birth_date")
    val gender = enumerationByName<UserData.UserGender>("gender", 20)
    val cityOfInterest = varchar("city_of_interest", 150)
    val private = bool("is_private")
    val profileImageUrl = varchar("profile_image_url", 200)
}

/**
 * @property id
 * @property email
 * @property passwordHash null when the account is created using oauth providers (google for this project)
 * @property name
 * @property surname
 * @property username
 * @property phone
 * @property birthDate
 * @property gender
 * @property cityOfInterest
 * @property private true if account is private, false for public accounts
 * @property profileImageUrl
 */
class UserEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserEntity>(UsersTable)

    var email by UsersTable.email
    var passwordHash by UsersTable.passwordHash
    var name by UsersTable.name
    var surname by UsersTable.surname
    var username by UsersTable.username
    var phone by UsersTable.phone
    var birthDate by UsersTable.birthDate
    var gender by UsersTable.gender
    var cityOfInterest by UsersTable.cityOfInterest
    var private by UsersTable.private
    var profileImageUrl by UsersTable.profileImageUrl
}

fun UserEntity.fromData(userData: UserData) {
    email = userData.email
    passwordHash = userData.passwordHash
    name = userData.name
    surname = userData.surname
    username = userData.username
    phone = userData.phone
    birthDate = userData.birthDate.toJavaLocalDate()
    gender = userData.gender
    cityOfInterest = userData.cityOfInterest
    private = userData.private
    profileImageUrl = userData.profileImageUrl
}

fun UserEntity.toData() = UserData(
    idUser = id.toIxId(),
    email = email,
    passwordHash = passwordHash,
    name = name,
    surname = surname,
    username = username,
    phone = phone,
    birthDate = birthDate.toKotlinLocalDate(),
    gender = gender,
    cityOfInterest = cityOfInterest,
    private = private,
    profileImageUrl = profileImageUrl
)

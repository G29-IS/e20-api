import app.e20.core.logic.PasswordEncoder
import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.impl.IxIdGenerator
import app.e20.core.logic.typedId.newIxId
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.schemas.user.UserEntity
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.toJavaLocalDate

private val log = KotlinLogging.logger {  }

/**
 * Generates a default user for the application
 */
fun main() {
    val defaultUser = UserData(
        idUser = newIxId(),
        email = "default@gmail.com",
        passwordHash = PasswordEncoder().encode("default"),
        name = "Marian",
        surname = "Ologu",
        username = "ologumarian",
        phone = "+393454568769",
        birthDate = kotlinx.datetime.LocalDate(2001, 5, 3),
        gender = UserData.UserGender.MALE,
        cityOfInterest = "tbd",
        private = false,
        profileImageUrl = ""
    )

    log.info { "Default user generated" }
    log.info { defaultUser }
}
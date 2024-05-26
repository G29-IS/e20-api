import app.e20.core.logic.PasswordEncoder
import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.impl.IxIdGenerator
import app.e20.core.logic.typedId.newIxId
import app.e20.data.models.user.UserData
import app.e20.data.sources.db.schemas.user.UserEntity
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.toJavaLocalDate
import org.slf4j.LoggerFactory

private val log = KotlinLogging.logger {  }

/**
 * Generates a default user for the application
 */
fun main() {
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = Level.INFO

    val defaultUser = UserData(
        idUser = newIxId(),
        email = "default@gmail.com",
        passwordHash = PasswordEncoder().encode("g29-password"),
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
package app.e20.core.logic

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

object DatetimeUtils {
    fun currentLocalDateTime(): LocalDateTime {
        val currentMoment: Instant = Clock.System.now()

        return currentMoment.toLocalDateTime(TimeZone.UTC)
    }

    fun currentInstant() = currentLocalDateTime().toInstant(TimeZone.UTC)

    fun currentMillis() = System.currentTimeMillis()
}


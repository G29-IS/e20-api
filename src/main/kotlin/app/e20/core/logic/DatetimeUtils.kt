package app.e20.core.logic

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

object DatetimeUtils {
    const val oneDayMillis = 24 * 60 * 60 * 1000

    fun currentLocalDateTime(): LocalDateTime {
        val currentMoment: Instant = Clock.System.now()

        return currentMoment.toLocalDateTime(TimeZone.UTC)
    }

    fun currentInstant() = currentLocalDateTime().toInstant(TimeZone.UTC)

    fun currentMillis() = System.currentTimeMillis()
}


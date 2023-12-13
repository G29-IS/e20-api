package app.e_20.core.logic

import java.time.ZoneOffset
import java.util.*

object DatetimeUtils {
    val utcTimeZone = TimeZone.getTimeZone(ZoneOffset.UTC)

    val oneDayMillis = 24 * 60 * 60 * 1000

    fun currentMillis() = System.currentTimeMillis()
}


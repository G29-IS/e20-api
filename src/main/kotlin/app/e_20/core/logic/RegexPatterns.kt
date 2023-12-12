package app.e_20.core.logic

/**
 * Commonly used RegEx patterns
 */
object RegexPatterns {
    val emailPattern = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])".toRegex()
    val colorPattern = "#[0-9a-fA-F]{6}".toRegex()
}

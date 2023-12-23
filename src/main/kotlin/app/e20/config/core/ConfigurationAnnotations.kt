package app.e20.config.core

/**
 * Represents a group of [ConfigurationProperty]
 */
@Target(AnnotationTarget.CLASS)
annotation class Configuration(
    val prefix: String
)

/**
 * Represents a single configuration variable
 */
@Target(AnnotationTarget.PROPERTY)
annotation class ConfigurationProperty(
    val name: String,
    val optional: Boolean = false
)
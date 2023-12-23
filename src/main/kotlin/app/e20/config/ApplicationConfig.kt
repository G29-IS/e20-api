package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty
import ch.qos.logback.classic.Level

@Configuration("application")
object ApplicationConfig {
    /**
     * Global log level for the application
     *
     * @see [Level]
     */
    @ConfigurationProperty("log.level")
    var logLevel: Level = Level.INFO
}
import app.e20.config.core.ConfigurationManager
import app.e20.config.core.ConfigurationReader
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import core.createScriptOutputsFolderIfNotExisting
import io.github.oshai.kotlinlogging.KotlinLogging
import org.slf4j.LoggerFactory
import java.io.File

private val log = KotlinLogging.logger {  }

/**
 * Script that generates a template .env file based on the declared @Configuration objects
 */
fun main() {
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = Level.INFO
    val configs = ConfigurationManager(
        packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
        configurationReader = ConfigurationReader::read
    ).listConfigurations()

    val folder = createScriptOutputsFolderIfNotExisting()
    val file = File(folder, ".env.template")
    file.writeText(configs.joinToString("\n") { it.toString() } )

    log.info { "Generated .env.template file in /script-outputs" }
}
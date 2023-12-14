package app.e_20

import app.e_20.api.plugins.*
import app.e_20.api.routing.configureRouting
import app.e_20.config.ApiConfig
import app.e_20.config.ApplicationConfig
import app.e_20.config.core.ConfigurationManager
import app.e_20.config.core.ConfigurationReader
import ch.qos.logback.classic.Logger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

private val logger = KotlinLogging.logger { }

fun main() {
    /**
     * Very important performance improvement ^^
     */
    val cmdLineLogo = """
        
        __/\\\\\\\\\\\\\\\____/\\\\\\\\\_________/\\\\\\\____        
         _\/\\\///////////___/\\\///////\\\_____/\\\/////\\\__       
          _\/\\\_____________\///______\//\\\___/\\\____\//\\\_      
           _\/\\\\\\\\\\\_______________/\\\/___\/\\\_____\/\\\_     
            _\/\\\///////_____________/\\\//_____\/\\\_____\/\\\_    
             _\/\\\_________________/\\\//________\/\\\_____\/\\\_   
              _\/\\\_______________/\\\/___________\//\\\____/\\\__  
               _\/\\\\\\\\\\\\\\\__/\\\\\\\\\\\\\\\__\///\\\\\\\/___ 
                _\///////////////__\///////////////_____\///////_____
                
    """.trimIndent()

    println(cmdLineLogo)

    /**
     * Load configuration properties (environment)
     */
    val configInitializer = ConfigurationManager(
        packageName = ConfigurationManager.DEFAULT_CONFIG_PACKAGE,
        ConfigurationReader::read
    )

    configInitializer.initialize()


    /**
     * Configure logging
     */
    (LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME) as Logger).level = ApplicationConfig.logLevel


    /**
     * Launch api server
     */
    embeddedServer(Netty, port = ApiConfig.port, host = "0.0.0.0", module = Application::indexApplicationModule)
        .start(wait = true)
}

private fun Application.indexApplicationModule() {
    configureDI()
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureValidator()
    configureRouting()
    configureSwagger()
}

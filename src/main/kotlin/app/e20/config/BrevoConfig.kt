package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty

/**
 * Properties for interacting with the [Brevo transactional email](https://www.brevo.com/it/products/transactional-email/) service
 */
@Configuration("brevo")
object BrevoConfig {
    /**
     * Api key for sending authenticated requests to Brevo
     */
    @ConfigurationProperty("api.key")
    var apiKey: String = "none"

    /**
     * Base url for the password reset url that the user will receive in the mail when requesting to reset its password
     */
    @ConfigurationProperty("reset.password.url")
    var passwordResetUrl: String = "http://localhost:8080/reset-password-webpage"
}
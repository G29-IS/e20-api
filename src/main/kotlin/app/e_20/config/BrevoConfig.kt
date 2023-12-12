package app.e_20.config

import app.e_20.config.core.Configuration
import app.e_20.config.core.ConfigurationProperty

/**
 * Properties for interacting with the [Brevo transactional email](https://www.brevo.com/it/products/transactional-email/) service
 */
@Configuration("brevo")
object BrevoConfig {
    /**
     * Api key for sending authenticated requests to Brevo
     */
    @ConfigurationProperty("api.key")
    lateinit var apiKey: String

    /**
     * Base url for the password reset url that the user will receive in the mail when requesting to reset its password
     */
    @ConfigurationProperty("reset.password.url")
    lateinit var passwordResetUrl: String
}
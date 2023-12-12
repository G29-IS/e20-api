package app.e_20.config

import app.e_20.config.core.Configuration
import app.e_20.config.core.ConfigurationProperty

@Configuration("oauth")
object OAuthConfig {
    /**
     * TODO:
     */
    @ConfigurationProperty("google.client.id")
    lateinit var googleClientId: String

    /**
     * TODO:
     */
    @ConfigurationProperty("google.client.secret")
    lateinit var googleClientSecret: String

    /**
     * TODO:
     */
    @ConfigurationProperty("google.redirect.uri")
    lateinit var googleRedirectUri: String
}
package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty

@Configuration("oauth")
object OAuthConfig {
    @ConfigurationProperty("google.client.id")
    var googleClientId: String = "none"

    @ConfigurationProperty("google.client.secret")
    var googleClientSecret: String = "none"

    @ConfigurationProperty("google.redirect.uri")
    var googleRedirectUri: String = "none"
}
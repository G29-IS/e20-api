package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty

@Configuration("sentry")
object SentryConfig {
    /**
     * Needed for logging errors to Sentry
     * **Must be set as an environment variable**
     *
     * Auth token can be obtained here
     * https://e20.sentry.io/projects/api/getting-started/?product=performance-monitoring#:~:text=Organization%20Auth%20Tokens
     */
    @ConfigurationProperty("auth.token")
    var authToken = "secret"
}
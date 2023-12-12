package app.e_20.config

import app.e_20.config.core.Configuration
import app.e_20.config.core.ConfigurationProperty

@Configuration("api")
object ApiConfig {
    /**
     * Port for the webserver
     */
    @ConfigurationProperty("port")
    var port: Int = 8080

    /**
     * Max age in seconds of a user authentication session
     */
    @ConfigurationProperty("session.max.age.in.seconds")
    var sessionMaxAgeInSeconds: Long = 604800 // 7 days by default

    /**
     * The secret for jwt auth tokens
     */
    @ConfigurationProperty("jwt.secret")
    lateinit var jwtSecret: String


    /**
     * The issuer for jwt auth tokens
     */
    @ConfigurationProperty("jwt.issuer")
    lateinit var jwtIssuer: String

    /**
     * The audience for jwt auth tokens
     */
    @ConfigurationProperty("jwt.audience")
    lateinit var jwtAudience: String

    /**
     * The real for jwt auth tokens
     */
    @ConfigurationProperty("jwt.realm")
    lateinit var jwtRealm: String
}
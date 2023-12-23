package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty

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
    var jwtSecret: String = "igMea8KNpLKoAyKLodKbfrkLioTMBahmaCxfq83fKyKLbfrkLioTMBahNpLKoAodKbfrkLioTMBahmaCxfq83fKbf"

    /**
     * The issuer for jwt auth tokens
     */
    @ConfigurationProperty("jwt.issuer")
    var jwtIssuer: String = "http://0.0.0.0:8080/"

    /**
     * The audience for jwt auth tokens
     */
    @ConfigurationProperty("jwt.audience")
    var jwtAudience: String = "http://0.0.0.0:8080/"

    /**
     * The real for jwt auth tokens
     */
    @ConfigurationProperty("jwt.realm")
    var jwtRealm: String = "user-routes"
}
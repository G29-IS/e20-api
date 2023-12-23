package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty

@Configuration("postgres")
object PostgresConfig {
    /**
     * Postgres server url
     */
    @ConfigurationProperty("url")
    var url: String = "jdbc:postgresql://localhost:5432/e20devdb"

    /**
     * Postgres' authentication user
     */
    @ConfigurationProperty("user")
    var user: String = "e20DevUser"

    /**
     * Postgres' authentication password
     */
    @ConfigurationProperty("password")
    var password: String = "e20DevPassword"
}
package app.e_20.config

import app.e_20.config.core.Configuration
import app.e_20.config.core.ConfigurationProperty

@Configuration("postgres")
object PostgresConfig {
    /**
     * Postgres server url
     */
    @ConfigurationProperty("url")
    lateinit var url: String

    /**
     * Postgres' authentication user
     */
    @ConfigurationProperty("user")
    lateinit var user: String

    /**
     * Postgres' authentication password
     */
    @ConfigurationProperty("password")
    lateinit var password: String
}
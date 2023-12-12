package app.e_20.config

import app.e_20.config.core.Configuration
import app.e_20.config.core.ConfigurationProperty

@Configuration("redis")
object RedisConfig {
    /**
     * Connection string uri for connecting to Redis
     */
    @ConfigurationProperty("connection.string")
    lateinit var connectionString: String
}
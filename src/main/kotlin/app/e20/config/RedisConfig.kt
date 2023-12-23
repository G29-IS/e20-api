package app.e20.config

import app.e20.config.core.Configuration
import app.e20.config.core.ConfigurationProperty

@Configuration("redis")
object RedisConfig {
    /**
     * Connection string uri for connecting to Redis
     */
    @ConfigurationProperty("connection.string")
    var connectionString: String = "redis://localhost:6379"
}
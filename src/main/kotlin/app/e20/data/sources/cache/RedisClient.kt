package app.e20.data.sources.cache

import app.e20.config.RedisConfig
import org.koin.core.annotation.Single
import redis.clients.jedis.JedisPool

/**
 * Redis client
 *
 * Use [close] when stopping the application
 */
@Single(createdAtStart = true)
class RedisClient {
    /**
     * Redis connection pool
     */
    val jedisPool = JedisPool(RedisConfig.connectionString)

    fun close() {
        jedisPool.close()
    }
}

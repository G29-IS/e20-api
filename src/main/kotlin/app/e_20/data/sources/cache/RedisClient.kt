package app.e_20.data.sources.cache

import app.e_20.config.RedisConfig
import redis.clients.jedis.JedisPool

/**
 * Redis client
 *
 * Use [close] when stopping the application
 */
class RedisClient {
    /**
     * Redis connection pool
     */
    val jedisPool = JedisPool(RedisConfig.connectionString)

    fun close() {
        jedisPool.close()
    }
}

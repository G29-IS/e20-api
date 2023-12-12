package app.e_20.data.sources.cache

import app.e_20.config.RedisConfig
import app.e_20.data.sources.cache.RedisClient.close
import redis.clients.jedis.JedisPool

/**
 * Redis client
 *
 * Use [close] when stopping the application
 */
object RedisClient {
    /**
     * Redis connection pool
     */
    val jedisPool = JedisPool(RedisConfig.connectionString)

    fun close() {
        jedisPool.close()
    }
}

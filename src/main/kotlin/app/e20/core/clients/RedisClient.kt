package app.e20.core.clients

import app.e20.config.RedisConfig
import app.e20.di.IClosableComponent
import org.koin.core.annotation.Single
import redis.clients.jedis.JedisPool

@Single(createdAtStart = true)
class RedisClient : IClosableComponent {
    /**
     * Redis connection pool
     */
    val jedisPool = JedisPool(RedisConfig.connectionString)

    override suspend fun close() {
        jedisPool.close()
    }
}

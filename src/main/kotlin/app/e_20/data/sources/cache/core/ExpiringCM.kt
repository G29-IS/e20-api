package app.e_20.data.sources.cache.core

import app.e_20.core.logic.ObjectMapper
import app.e_20.data.sources.cache.RedisClient

/**
 * Cache manager that allows to set an expiration time for each key.
 *
 * Also allows for a base name that applies to all keys
 *
 * @param keyBase the base for all the keys
 * @param expirationInSeconds expiration time for all keys
 */
abstract class ExpiringCM(
    private val keyBase: String,
    val expirationInSeconds: Long
) {
    /**
     * Constructs a key from the base + dynamic
     */
    protected fun keyName(hashValue: String) = "${keyBase}:$hashValue"

    /**
     * Get data from a key
     */
    protected inline fun <reified T> get(keyValue: String): T? {
        RedisClient.jedisPool.resource.use {
            val json = it.get(keyName(keyValue))
            return if (json != null) ObjectMapper.decode(json) else null
        }
    }

    /**
     * Cache data in a key
     */
    protected inline fun <reified T> cache(keyValue: String, data: T) {
        RedisClient.jedisPool.resource.use {
            val json = ObjectMapper.encode(data)
            it.setex(keyName(keyValue), expirationInSeconds, json)
        }
    }

    /**
     * Delete a key
     */
    protected fun delete(keyValue: String) {
        RedisClient.jedisPool.resource.use {
            it.del(keyName(keyValue))
        }
    }
}

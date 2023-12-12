package app.e_20.core.logic

import app.e_20.core.logic.ObjectMapper.decode
import app.e_20.core.logic.ObjectMapper.decodeFromByteArray
import app.e_20.core.logic.ObjectMapper.decodeList
import app.e_20.core.logic.ObjectMapper.encode
import app.e_20.core.logic.ObjectMapper.encodeToByteArray
import app.e_20.core.logic.typedId.serialization.IdKotlinXSerializationModule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Object serializer and deserializer
 *
 * @see encode
 * @see encodeToByteArray
 * @see decode
 * @see decodeFromByteArray
 * @see decodeList
 */
object ObjectMapper {
    val json = Json {
        serializersModule = IdKotlinXSerializationModule
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    /**
     * Serialize [data]
     *
     * @param data The data to serialize
     * @return The serialized [data] as [String]
     */
    inline fun <reified T> encode(data: T): String {
        return json.encodeToString(data)
    }

    /**
     * Serialize [data] to a byte array
     *
     * @param data The data to serialize
     * @return The serialized [data] as [ByteArray]
     */
    inline fun <reified T> encodeToByteArray(data: T): ByteArray {
        return json.encodeToString(data).encodeToByteArray()
    }

    /**
     * Deserialize [data]
     *
     * @param data The data to deserialize
     *
     * @throws IllegalArgumentException if [data] is not a valid instance of [T]
     * @return The deserialized [data] as [T]
     */
    inline fun <reified T> decode(data: String): T {
        return json.decodeFromString(data)
    }

    /**
     * Deserialize [data] of [ByteArray]
     *
     * @param data The data to deserialize
     *
     * @throws IllegalArgumentException if [data] is not a valid instance of [T]
     * @return The deserialized [data] as [T]
     */
    inline fun <reified T> decodeFromByteArray(data: ByteArray): T {
        return json.decodeFromString(data.decodeToString())
    }

    /**
     * Deserialize [listOfData]
     *
     * @param listOfData The data to deserialize
     *
     * @throws IllegalArgumentException if [listOfData] is not a valid [List] of [T]
     * @return The deserialized [listOfData] as [List] of [T]
     */
    inline fun <reified T> decodeList(listOfData: MutableCollection<String>): List<T> {
        return json.decodeFromString("[${listOfData.joinToString(", ")}]")
    }
}

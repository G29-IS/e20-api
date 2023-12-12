package app.e_20.core.logic

import app.e_20.core.logic.TokenGenerator.generate
import app.e_20.core.logic.TokenGenerator.hashToken
import io.github.oshai.kotlinlogging.KotlinLogging
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*

private val log = KotlinLogging.logger {  }

/**
 * Generator for secure random tokens
 *
 * @see generate
 * @see hashToken
 */
object TokenGenerator {
    private val secureRandom: SecureRandom = SecureRandom() // thread safe
    private val base64Encoder = Base64.getUrlEncoder()
    private val base64Decoder = Base64.getUrlDecoder()

    /**
     * Generates a secure random token
     *
     * @param bytes The number of bytes to use for the token, default is 16
     * @return A pair, where the first value is the token in base64 url safe format, and the second value is the hash of the token also in base64 url safe format
     */
    fun generate(bytes: Int = 16): Pair<String, String> {
        val plainBytes = ByteArray(bytes)
        secureRandom.nextBytes(plainBytes)

        // MessageDigest is not thread safe and getInstance doesn't refer to a singleton
        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashed = messageDigest.digest(plainBytes)

        log.debug { "Generated token of $bytes bytes" }

        return Pair(base64Encoder.encodeToString(plainBytes), base64Encoder.encodeToString(hashed))
    }

    /**
     * Hashes a give [token] encoded in base64 format
     */
    fun hashToken(token: String): String {
        val bytes = base64Decoder.decode(token)

        val messageDigest = MessageDigest.getInstance("SHA-256")
        val hashed = messageDigest.digest(bytes)

        return base64Encoder.encodeToString(hashed)
    }
}

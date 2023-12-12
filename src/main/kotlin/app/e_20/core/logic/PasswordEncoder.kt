package app.e_20.core.logic

import app.e_20.core.logic.PasswordEncoder.encode
import app.e_20.core.logic.PasswordEncoder.matches
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * Password encoder and matcher
 *
 * @see encode
 * @see matches
 */
object PasswordEncoder {
    private val bcryptPasswordEncoder = BCryptPasswordEncoder()

    /**
     * Encodes a password to [String]
     *
     * @return the encoded password
     */
    fun encode(password: String): String =
        bcryptPasswordEncoder.encode(password)

    /**
     * Checks whether a [rawPassword] and an [encodedPassword] are the same
     *
     * @return true if they match, false otherwise
     */
    fun matches(rawPassword: String, encodedPassword: String) =
        bcryptPasswordEncoder.matches(rawPassword, encodedPassword)
}

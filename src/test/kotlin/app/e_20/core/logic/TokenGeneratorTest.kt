package app.e_20.core.logic

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TokenGeneratorTest {

    @Test
    fun hashToken() {
        val (token, hashed) = TokenGenerator.generate()

        assertEquals(hashed, TokenGenerator.hashToken(token))
    }
}

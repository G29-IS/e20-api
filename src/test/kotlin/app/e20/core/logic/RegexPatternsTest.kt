package app.e20.core.logic

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

class RegexPatternsTest {

    @Test
    fun testEmailPattern() {
        val shouldMatch = "test@gmail.com"
        val shouldNotMatch = "testgmail.com"

        assert(RegexPatterns.emailPattern.matches(shouldMatch))
        assertFalse(RegexPatterns.emailPattern.matches(shouldNotMatch))
    }
}
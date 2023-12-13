package app.e_20.core.clients.oauth

import app.e_20.config.OAuthConfig
import app.e_20.data.models.oauth.google.GoogleUserInfoDto
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.apache.v2.ApacheHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory

/**
 * Client to interact with Google OAuth service
 */
class GoogleOAuthClient {
    private val transport: HttpTransport = ApacheHttpTransport()
    private val jsonFactory: JsonFactory = GsonFactory()

    private val verifier = GoogleIdTokenVerifier.Builder(transport, jsonFactory)
        .setAudience(listOf(OAuthConfig.googleClientId))
        .build()

    /**
     * Verifies a user [token] and gets the user info if valid
     *
     * @param token
     * @return [GoogleUserInfoDto]
     */
    fun getUserInfoFromIdTokenIfValid(token: String): GoogleUserInfoDto? {
        val idToken = verifier.verify(token) ?: return null
        val payload = idToken.payload

        return GoogleUserInfoDto(
            email = payload.email,
        )
    }
}
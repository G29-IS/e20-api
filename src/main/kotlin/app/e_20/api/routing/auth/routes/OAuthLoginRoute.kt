package app.e_20.api.routing.auth.routes

import app.e_20.api.plugins.JwtClaims
import app.e_20.api.routing.auth.LoginWithGoogle
import app.e_20.config.ApiConfig
import app.e_20.core.clients.oauth.GoogleOAuthClient
import app.e_20.core.exceptions.AuthenticationException
import app.e_20.data.daos.auth.UserSessionDao
import app.e_20.data.daos.auth.impl.UserSessionDaoCacheImpl
import app.e_20.data.daos.user.UserDao
import app.e_20.data.daos.user.impl.UserDaoImpl
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.oauthLoginRoutes() {
    val userDao by inject<UserDao>()
    val userSessionDao by inject<UserSessionDao>()
    val googleOAuthClient by inject<GoogleOAuthClient>()

    get<LoginWithGoogle>({
        tags = listOf("auth")
        operationId = "login-with-google"
        summary = "google oauth login"
        description = "the user needs to get an id token with google oauth and forward it to this endpoint to get authenticated via google"
        protected = false
        request {
            queryParameter<String>("tokenId") {
                description = "the id token received from google"
                required = true
                allowEmptyValue = false
                allowReserved = false
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "user authenticated"
                body("jwt-token-response-schema") {
                    description = "jwt token for authentication trough the Authentication header"
                }
            }
            HttpStatusCode.Unauthorized to {
                description = "invalid id token or user not found"
            }
        }
    }) {
        val userInfo = googleOAuthClient.getUserInfoFromIdTokenIfValid(it.tokenId)
            ?: throw AuthenticationException()

        // Find the existing user
        val user = userDao.getFromEmail(userInfo.email)
            ?: throw AuthenticationException()

        // Send jwt token
        val sessionId = userSessionDao.create(user.id, call.request.userAgent(), call.request.origin.remoteAddress)

        val token = JWT.create()
            .withAudience(ApiConfig.jwtAudience)
            .withIssuer(ApiConfig.jwtIssuer)
            .withClaim(JwtClaims.JWT_SESSION_ID_CLAIM, sessionId.toString())
            .withClaim(JwtClaims.JWT_USER_ID_CLAIM, user.id.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + (ApiConfig.sessionMaxAgeInSeconds * 1000)))
            .sign(Algorithm.HMAC256(ApiConfig.jwtSecret))

        call.respond(hashMapOf("token" to token))
    }
}

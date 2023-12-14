package app.e_20.api.routing.auth.routes

import app.e_20.api.plugins.JwtClaims
import app.e_20.api.routing.auth.LoginRoute
import app.e_20.config.ApiConfig
import app.e_20.core.exceptions.AuthenticationException
import app.e_20.core.logic.PasswordEncoder
import app.e_20.data.daos.auth.UserSessionDao
import app.e_20.data.daos.user.UserDao
import app.e_20.data.models.auth.LoginCredentials
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.loginRoute() {
    val userDao by inject<UserDao>()
    val userSessionDao by inject<UserSessionDao>()
    val passwordEncoder by inject<PasswordEncoder>()

    post<LoginRoute>({
        tags = listOf("auth")
        operationId = "login"
        summary = "login and create a session"
        protected = false
        request {
            body<LoginCredentials> {
                description = "email and password credentials"
                required = true
                example("sample-credentials", LoginCredentials("sample@mail.com", "verySecurePwd1234"))
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "user authenticated and session created"
                body("jwt-token-response-schema") {
                    description = "jwt token for authentication trough the Authentication header"
                }
            }
            HttpStatusCode.Unauthorized to {
                description = "invalid credentials"
            }
        }
    }) {
        val loginData = call.receive<LoginCredentials>()
        val user = userDao.getFromEmail(loginData.email)
            ?: throw AuthenticationException()

        if (user.passwordHash == null)
            throw AuthenticationException()

        if (!passwordEncoder.matches(loginData.password, user.passwordHash))
            throw AuthenticationException()

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

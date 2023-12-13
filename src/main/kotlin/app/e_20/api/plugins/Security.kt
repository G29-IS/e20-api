package app.e_20.api.plugins

import app.e_20.config.ApiConfig
import app.e_20.core.logic.DatetimeUtils
import app.e_20.core.logic.typedId.impl.IxId
import app.e_20.core.logic.typedId.toIxId
import app.e_20.data.daos.auth.UserSessionDao
import app.e_20.data.daos.auth.impl.UserSessionDaoCacheImpl
import app.e_20.data.models.auth.UserAuthSessionDto
import app.e_20.data.models.user.UserDto
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import org.koin.ktor.ext.inject

/**
 * Available authentication methods for api routes
 */
object AuthenticationMethods {
    const val USER_SESSION_AUTH = "user_session_auth"
}

object JwtClaims {
    /**
     * Default claim name for the user_id field inside a jwt token payload
     */
    const val JWT_USER_ID_CLAIM = "user_id"

    /**
     * Default claim name for the session_id field inside a jwt token payload
     */
    const val JWT_SESSION_ID_CLAIM = "session_id"
}

/**
 * Gets the Id of a UserDto from the auth-user-session UserSessionDto
 */
fun PipelineContext<Unit, ApplicationCall>.userIdFromSession(): IxId<UserDto>? = call.principal<UserAuthSessionDto>()?.userId

fun Application.configureSecurity() {
    val userSessionDao by inject<UserSessionDao>()

    install(Authentication) {

        jwt(AuthenticationMethods.USER_SESSION_AUTH) {
            realm = ApiConfig.jwtRealm

            verifier(JWT
                .require(Algorithm.HMAC256(ApiConfig.jwtSecret))
                .withAudience(ApiConfig.jwtAudience)
                .withIssuer(ApiConfig.jwtIssuer)
                .build()
            )

            validate { credentials ->
                val userId: IxId<UserDto> = credentials.payload.getClaim(JwtClaims.JWT_SESSION_ID_CLAIM).asString().toIxId()
                val sessionId: IxId<UserAuthSessionDto> = credentials.payload.getClaim(JwtClaims.JWT_SESSION_ID_CLAIM).asString().toIxId()

                val session = userSessionDao.get(userId, sessionId)

                // If there is no session or if it has expired
                if (session == null || (DatetimeUtils.currentMillis() - session.iat) >= (ApiConfig.sessionMaxAgeInSeconds * 1000))
                    null
                else
                    session
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}

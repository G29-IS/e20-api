package app.e20.api.plugins

import app.e20.config.ApiConfig
import app.e20.core.exceptions.AuthenticationException
import app.e20.core.logic.DatetimeUtils
import app.e20.core.logic.typedId.impl.IxId
import app.e20.core.logic.typedId.toIxId
import app.e20.data.daos.auth.UserSessionDao
import app.e20.data.models.auth.UserAuthSessionData
import app.e20.data.models.user.UserData
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
 * Gets the [IxId] of a [UserData] from the auth-user-session [UserAuthSessionData]
 */
fun PipelineContext<Unit, ApplicationCall>.userIdFromSession(): IxId<UserData>? = call.principal<UserAuthSessionData>()?.userId

/**
 * Gets the [IxId] of a [UserData] from the auth-user-session [UserAuthSessionData]
 *
 * @throws AuthenticationException
 */
fun PipelineContext<Unit, ApplicationCall>.userIdFromSessionOrThrow(): IxId<UserData> = call.principal<UserAuthSessionData>()?.userId ?: throw AuthenticationException()

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
                val userId: IxId<UserData> = credentials.payload.getClaim(JwtClaims.JWT_USER_ID_CLAIM)
                    .asString()
                    .takeIf { it.isNotEmpty() }
                    ?.toIxId()
                    ?: return@validate null
                val sessionId: IxId<UserAuthSessionData> = credentials.payload.getClaim(JwtClaims.JWT_SESSION_ID_CLAIM)
                    .asString()
                    .takeIf { it.isNotEmpty() }
                    ?.toIxId()
                    ?: return@validate null

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

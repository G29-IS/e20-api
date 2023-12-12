package app.e_20.api.plugins

import app.e_20.core.exceptions.AuthenticationException
import app.e_20.core.exceptions.AuthorizationException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.util.logging.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<AuthenticationException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> { call, _ ->
            call.respond(HttpStatusCode.Forbidden)
        }
        exception<RequestValidationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.reasons.joinToString("\n"))
        }
        exception<Exception> { call, cause ->
            call.application.environment.log.error(cause)
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
}

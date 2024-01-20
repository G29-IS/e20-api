package app.e20.api.routing.user.routes

import app.e20.api.routing.user.LogoutRoute
import app.e20.core.exceptions.AuthenticationException
import app.e20.data.daos.auth.UserSessionDao
import app.e20.data.models.auth.UserAuthSessionData
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.logoutRoute() {
    val userSessionDao by inject<UserSessionDao>()

    get<LogoutRoute>({
        tags = listOf("auth")
        operationId = "logout"
        summary = "terminates the auth session"
        response {
            HttpStatusCode.OK to {
                description = "session terminated"
            }

            HttpStatusCode.Unauthorized to {
                description = "not logged in, only logged in users can delete events"
            }
        }
    }) {
        val sessionData = call.principal<UserAuthSessionData>()
            ?: throw AuthenticationException()

        userSessionDao.delete(sessionData.userId, sessionData.id)

        call.respond(HttpStatusCode.OK)
    }
}

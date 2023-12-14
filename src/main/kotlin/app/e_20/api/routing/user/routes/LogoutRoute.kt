package app.e_20.api.routing.user.routes

import app.e_20.api.routing.user.LogoutRoute
import app.e_20.data.daos.auth.UserSessionDao
import app.e_20.data.models.auth.UserAuthSessionDto
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
        }
    }) {
        val sessionData = call.principal<UserAuthSessionDto>()!!

        userSessionDao.delete(sessionData.userId, sessionData.id)

        call.respond(HttpStatusCode.OK)
    }
}

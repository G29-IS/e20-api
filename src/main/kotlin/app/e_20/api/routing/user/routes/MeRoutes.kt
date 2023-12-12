package app.e_20.api.routing.user.routes

import app.e_20.api.plugins.userIdFromSession
import app.e_20.api.routing.user.MeRoute
import app.e_20.core.exceptions.AuthenticationException
import app.e_20.data.daos.user.UserDao
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.meRoutes() {
    get<MeRoute>({
        tags = listOf("user")
        operationId = "me"
        summary = "get the logged in user data"
        response {
            HttpStatusCode.OK to {
                description = "user data"
            }
        }
    }) {
        val user = UserDao.get(userIdFromSession()!!)
            ?: throw AuthenticationException()

        call.respond(user.getResponseDto())
    }
}

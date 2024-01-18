package app.e20.api.routing.user.routes

import app.e20.api.plugins.userIdFromSessionOrThrow
import app.e20.api.routing.user.MeRoute
import app.e20.core.exceptions.AuthenticationException
import app.e20.data.daos.user.UserDao
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.meRoutes() {
    val userDao by inject<UserDao>()

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
        val user = userDao.get(userIdFromSessionOrThrow())
            ?: throw AuthenticationException()

        call.respond(user)
    }
}

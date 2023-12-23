package app.e20.api.routing.user.routes

import app.e20.api.routing.user.UserRoute
import app.e20.data.daos.user.UserDao
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {
    val userDao by inject<UserDao>()

    get<UserRoute>({
        tags = listOf("user")
        operationId = "get-user"
        summary = "gets a single user"
    }) {
        val user = userDao.get(it.id)
            ?: call.respond(HttpStatusCode.NotFound)

        call.respond(user)
    }
}
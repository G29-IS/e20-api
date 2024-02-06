package app.e20.api.routing.user.routes

import app.e20.api.routing.user.UserRoute
import app.e20.data.daos.event.EventDao
import app.e20.data.daos.user.UserDao
import app.e20.data.models.user.UserData
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoute() {
    val userDao by inject<UserDao>()
    val eventDao by inject<EventDao>()

    get<UserRoute>({
        tags = listOf("user")
        operationId = "get-user"
        summary = "gets a single user"
        request {
            pathParameter<String>("id") {
                required = true
                description = "the id of the user"
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "user data with events organized by the user"
                body<UserData.UserWithEventsOrganizedData>()
            }
            HttpStatusCode.NotFound to {
                description = "user not found"
            }
        }
    }) {
        val user = userDao.get(it.id)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        val eventsOrganized = eventDao.getOfOrganizer(it.id)

        call.respond(UserData.UserWithEventsOrganizedData(
            user = user.toUserPublicData(),
            eventsOrganized = eventsOrganized
        ))
    }
}
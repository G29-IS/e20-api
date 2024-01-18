package app.e20.api.routing.user

import app.e20.api.plugins.AuthenticationMethods
import app.e20.api.routing.user.routes.logoutRoute
import app.e20.api.routing.user.routes.meRoutes
import app.e20.api.routing.user.routes.passwordOperationRoutes
import app.e20.api.routing.user.routes.userRoute
import app.e20.core.logic.typedId.impl.IxId
import app.e20.data.models.user.UserData
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import kotlinx.serialization.Contextual

@Resource("/logout")
class LogoutRoute

@Resource("/password-forgotten")
class PasswordForgottenRoute(val email: String)

@Resource("/reset-password")
class ResetPasswordRoute(val token: String)

@Resource("/reset-password-webpage")
class ResetPasswordWebpageRoute(val token: String)

@Resource("/me")
class MeRoute

@Resource("/users/{id}")
class UserRoute(@Contextual val id: IxId<UserData>)

fun Route.userRoutes() {
    passwordOperationRoutes()

    authenticate(AuthenticationMethods.USER_SESSION_AUTH) {
        logoutRoute()
        meRoutes()
        userRoute()
    }
}

package app.e20.api.routing.user

import app.e20.api.plugins.AuthenticationMethods
import app.e20.api.routing.user.routes.logoutRoute
import app.e20.api.routing.user.routes.meRoutes
import app.e20.api.routing.user.routes.passwordOperationRoutes
import io.ktor.resources.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

@Resource("/logout")
class LogoutRoute

@Resource("/password-forgotten")
class PasswordForgottenRoute(val email: String)

@Resource("/reset-password")
class ResetPasswordRoute(val token: String)

@Resource("/me")
class MeRoute

fun Route.userRoutes() {
    passwordOperationRoutes()

    authenticate(AuthenticationMethods.USER_SESSION_AUTH) {
        logoutRoute()
        meRoutes()
    }
}

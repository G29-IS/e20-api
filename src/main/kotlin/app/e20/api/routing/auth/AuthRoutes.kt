package app.e20.api.routing.auth

import app.e20.api.routing.auth.routes.loginRoute
import app.e20.api.routing.auth.routes.oauthLoginRoutes
import io.ktor.resources.*
import io.ktor.server.routing.*

@Resource("/login")
class LoginRoute

@Resource("/login-with-google")
class LoginWithGoogle(val tokenId: String)


fun Route.authRoutes() {
    loginRoute()
    oauthLoginRoutes()
}

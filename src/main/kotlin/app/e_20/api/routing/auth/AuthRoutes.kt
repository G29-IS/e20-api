package app.e_20.api.routing.auth

import app.e_20.api.routing.auth.routes.loginRoute
import app.e_20.api.routing.auth.routes.oauthLoginRoutes
import app.e_20.data.daos.user.UserDao
import io.ktor.resources.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

@Resource("/login")
class LoginRoute

@Resource("/login-with-google")
class LoginWithGoogle(val tokenId: String)


fun Route.authRoutes() {
    loginRoute()
    oauthLoginRoutes()
}

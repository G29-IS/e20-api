package app.e20.api.routing.user.routes

import app.e20.api.routing.user.PasswordForgottenRoute
import app.e20.api.routing.user.ResetPasswordRoute
import app.e20.api.routing.user.ResetPasswordWebpageRoute
import app.e20.core.clients.BrevoClient
import app.e20.core.logic.PasswordEncoder
import app.e20.data.daos.auth.PasswordResetDao
import app.e20.data.daos.auth.UserSessionDao
import app.e20.data.daos.user.UserDao
import app.e20.data.models.auth.PasswordResetRequestBody
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*
import org.koin.ktor.ext.inject

fun Route.passwordOperationRoutes() {
    val userDao by inject<UserDao>()
    val userSessionDao by inject<UserSessionDao>()
    val passwordResetDao by inject<PasswordResetDao>()
    val passwordEncoder by inject<PasswordEncoder>()
    val brevoClient by inject<BrevoClient>()

    get<PasswordForgottenRoute>({
        tags = listOf("auth")
        operationId = "password-forgotten"
        summary = "request a password reset email"
        description = "will send an email with instructions on how to reset the password, this is subject to rate limits as with all email operations"
        protected = false
        request {
            queryParameter<String>("email") {
                description = "the encoded email of the user"
                example = "sample%40mail.com"
                required = true
                allowEmptyValue = false
                allowReserved = false
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "email sent"
            }
            HttpStatusCode.NotFound to {
                description = "something went wrong"
            }
            HttpStatusCode.TooManyRequests to {
                description = "action rate limited"
            }
        }
    }) { request ->
        val user = userDao.getFromEmail(request.email)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        if (passwordResetDao.isRateLimited(user.idUser))
            return@get call.respond(HttpStatusCode.TooManyRequests)

        val sentEmail = passwordResetDao.createAndSend(user)

        if (sentEmail)
            call.respond(HttpStatusCode.OK)
        else
            call.respond(HttpStatusCode.InternalServerError)
    }

    get<ResetPasswordWebpageRoute>({
        tags = listOf("webpage")
        operationId = "reset-password-webpage"
        summary = "html page to reset the password"
        description = "this html page is accessible from a link sent to the user email when he requests to reset its password"
        protected = false
        request {
            queryParameter<String>("token") {
                description = "the password reset token"
                required = true
                allowEmptyValue = false
                allowReserved = false
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "html page"
            }
        }
    }) {
        call.respondHtml(HttpStatusCode.OK) {
            head {
                title {
                    +"E20 - Reset password"
                }
            }
            body {
                h1 {
                    +"Password reset form"
                }

                p {
                    +"Insert your new password in the text fields below"
                }

                form(
                    action = "/reset-password?token=${it.token}",
                    encType = FormEncType.applicationXWwwFormUrlEncoded,
                    method = FormMethod.post,
                ) {
                    p {
                        +"Password:"
                        passwordInput(name = "password")
                    }
                    p {
                        submitInput() { value = "Confirm password reset" }
                    }
                }
            }
        }
    }

    post<ResetPasswordRoute>({
        tags = listOf("auth")
        operationId = "reset-password"
        summary = "reset the password via an auth token"
        description = "a user can reset its password via a token that is sent via email when he requests it"
        protected = false
        request {
            queryParameter<String>("token") {
                description = "reset token"
                required = true
                allowEmptyValue = false
                allowReserved = false
            }
            body<PasswordResetRequestBody> {
                description = "contains the new password"
                required = true
            }
        }
        response {
            HttpStatusCode.OK to {
                description = "password reset"
            }
            HttpStatusCode.NotFound to {
                description = "something went wrong"
            }
        }
    }) { request ->
        val passwordResetDto = passwordResetDao.get(request.token)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val user = userDao.get(passwordResetDto.userId)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newPassword = call.receive<PasswordResetRequestBody>().password
        val newPasswordHashed = passwordEncoder.encode(newPassword)

        // If the user email wasn't verified before, now it can be considered verified
        userDao.resetPassword(passwordResetDto.userId, newPasswordHashed)

        // Invalidate all other user active sessions
        userSessionDao.deleteAllSessionsOfUser(passwordResetDto.userId)

        // Send notification email
        brevoClient.sendPasswordResetSuccessEmail(user.email)

        call.respond(HttpStatusCode.OK)
    }
}
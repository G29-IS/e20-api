package app.e_20.api.routing.user.routes

import app.e_20.api.routing.user.PasswordForgottenRoute
import app.e_20.api.routing.user.ResetPasswordRoute
import app.e_20.core.clients.BrevoClient
import app.e_20.core.logic.PasswordEncoder
import app.e_20.data.daos.auth.PasswordResetDao
import app.e_20.data.daos.auth.UserSessionDao
import app.e_20.data.daos.user.UserDao
import app.e_20.data.models.auth.PasswordResetRequestBody
import io.github.smiley4.ktorswaggerui.dsl.resources.get
import io.github.smiley4.ktorswaggerui.dsl.resources.post
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.passwordOperationRoutes() {
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
        val user = UserDao.getFromEmail(request.email)
            ?: return@get call.respond(HttpStatusCode.NotFound)

        if (PasswordResetDao.isRateLimited(user.id))
            return@get call.respond(HttpStatusCode.TooManyRequests)

        val sentEmail = PasswordResetDao.createAndSend(user)

        if (sentEmail)
            call.respond(HttpStatusCode.OK)
        else
            call.respond(HttpStatusCode.InternalServerError)
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
        val passwordResetDto = PasswordResetDao.get(request.token)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val user = UserDao.get(passwordResetDto.userId)
            ?: return@post call.respond(HttpStatusCode.NotFound)

        val newPassword = call.receive<PasswordResetRequestBody>().password
        val newPasswordHashed = PasswordEncoder.encode(newPassword)

        // If the user email wasn't verified before, now it can be considered verified
        UserDao.resetPassword(passwordResetDto.userId, newPasswordHashed)

        // Invalidate all other user active sessions
        UserSessionDao.deleteAllSessionsOfUser(passwordResetDto.userId)

        // Send notification email
        BrevoClient.sendPasswordResetSuccessEmail(user.email)

        call.respond(HttpStatusCode.OK)
    }
}

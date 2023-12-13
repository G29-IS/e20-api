package app.e_20.api.routing

import app.e_20.api.routing.auth.authRoutes
import app.e_20.api.routing.event.eventRoutes
import app.e_20.api.routing.monitoring.monitoringRoutes
import app.e_20.api.routing.user.userRoutes
import app.e_20.core.logic.typedId.serialization.IdKotlinXSerializationModule
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    // Needed for typed queries
    install(Resources) {
        serializersModule = IdKotlinXSerializationModule
    }

    routing {
        monitoringRoutes()
        authRoutes()
        userRoutes()
        eventRoutes()
    }
}

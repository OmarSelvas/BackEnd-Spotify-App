package com.example.infrastructure.plugins

import com.example.application.routes.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureApiRouting() {
    routing {
        get("/") {
            call.respondText("Music API - Ktor con Arquitectura Hexagonal")
        }

        get("/health") {
            call.respondText("OK")
        }

        // API Routes
        route("/api/v1") {
            artistaRoutes()
            albumRoutes()
            trackRoutes()
        }
    }
}
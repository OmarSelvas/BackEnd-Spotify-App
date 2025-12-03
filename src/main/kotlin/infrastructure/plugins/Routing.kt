package com.example.infrastructure.plugins

import com.example.application.routes.*
import com.example.application.services.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val artistaService by inject<ArtistaService>()
    val albumService by inject<AlbumService>()
    val trackService by inject<TrackService>()

    routing {
        // Ruta de bienvenida
        get("/") {
            call.respond(
                HttpStatusCode.OK,
                mapOf("message" to "API Spotify - CRUD completo",
                    "version" to "1.0.0",
                    "docs" to "/artistas, /albums, /tracks"
                )
            )
        }

        // Ruta de salud
        get("/estado") {
            call.respond(
                HttpStatusCode.OK,
                mapOf((
                        "status" to "OK"),             // Agregué clave explícita y coma
                    "message" to "API esta corriendo", // Clave explícita y coma
                    "credits" to "gracias a papdio funciona") // Clave explícita
                )
        }

        // Registrar rutas de cada entidad
        artistaRoutes(artistaService)
        albumRoutes(albumService)
        trackRoutes(trackService)
    }
}
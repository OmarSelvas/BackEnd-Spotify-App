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
                mapOf(
                    "message" to "API Spotify - CRUD completo",
                    "version" to "1.0.0",
                    "endpoints" to mapOf(
                        "artistas" to "/artistas",
                        "albums" to "/albums",
                        "tracks" to "/tracks"
                    )
                )
            )
        }

        // Ruta de salud
        get("/health") {
            call.respond(
                HttpStatusCode.OK,
                mapOf("OK" to "señal" to "API esta corriendo" to "gracias a papdio funciona")
            )
        }

        // Registrar rutas de cada entidad
        artistaRoutes(artistaService)
        albumRoutes(albumService)
        trackRoutes(trackService)
    }
}
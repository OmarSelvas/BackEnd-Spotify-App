package com.example.application.routes

import com.example.application.services.ArtistaService
import com.example.domain.models.CreateArtistaRequest
import com.example.domain.models.UpdateArtistaRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.artistaRoutes() {
    val service by inject<ArtistaService>()

    route("/artistas") {
        // GET /artistas - Obtener todos los artistas
        get {
            val artistas = service.getAll()
            call.respond(HttpStatusCode.OK, artistas)
        }

        // GET /artistas/{id} - Obtener artista por ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val artista = service.getById(id)
            if (artista != null) {
                call.respond(HttpStatusCode.OK, artista)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
            }
        }

        // GET /artistas/{id}/albums - Obtener artista con sus álbumes
        get("/{id}/albums") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val result = service.getWithAlbums(id)
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
            }
        }

        // POST /artistas - Crear nuevo artista
        post {
            val request = call.receive<CreateArtistaRequest>()
            val artista = service.create(request)
            call.respond(HttpStatusCode.Created, artista)
        }

        // PUT /artistas/{id} - Actualizar artista
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val request = call.receive<UpdateArtistaRequest>()
            val artista = service.update(id, request)

            if (artista != null) {
                call.respond(HttpStatusCode.OK, artista)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
            }
        }

        // DELETE /artistas/{id} - Eliminar artista
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val deleted = service.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
            }
        }
    }
}
package com.example.application.routes

import com.example.application.services.AlbumService
import com.example.domain.models.CreateAlbumRequest
import com.example.domain.models.UpdateAlbumRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.albumRoutes() {
    val service by inject<AlbumService>()

    route("/albums") {
        // GET /albums - Obtener todos los álbumes
        get {
            val albums = service.getAll()
            call.respond(HttpStatusCode.OK, albums)
        }

        // GET /albums/{id} - Obtener álbum por ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val album = service.getById(id)
            if (album != null) {
                call.respond(HttpStatusCode.OK, album)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
            }
        }

        // GET /albums/{id}/tracks - Obtener álbum con sus tracks
        get("/{id}/tracks") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val result = service.getWithTracks(id)
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
            }
        }

        // POST /albums - Crear nuevo álbum
        post {
            val request = call.receive<CreateAlbumRequest>()
            service.create(request).fold(
                onSuccess = { album -> call.respond(HttpStatusCode.Created, album) },
                onFailure = { e -> call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message)) }
            )
        }

        // PUT /albums/{id} - Actualizar álbum
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val request = call.receive<UpdateAlbumRequest>()
            val album = service.update(id, request)

            if (album != null) {
                call.respond(HttpStatusCode.OK, album)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
            }
        }

        // DELETE /albums/{id} - Eliminar álbum
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val deleted = service.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
            }
        }
    }
}
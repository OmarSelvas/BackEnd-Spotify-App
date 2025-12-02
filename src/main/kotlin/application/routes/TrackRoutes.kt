package com.example.application.routes

import com.example.application.services.TrackService
import com.example.domain.models.CreateTrackRequest
import com.example.domain.models.UpdateTrackRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.trackRoutes() {
    val service by inject<TrackService>()

    route("/tracks") {
        // GET /tracks - Obtener todos los tracks
        get {
            val tracks = service.getAll()
            call.respond(HttpStatusCode.OK, tracks)
        }

        // GET /tracks/{id} - Obtener track por ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val track = service.getById(id)
            if (track != null) {
                call.respond(HttpStatusCode.OK, track)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track not found"))
            }
        }

        // POST /tracks - Crear nuevo track
        post {
            val request = call.receive<CreateTrackRequest>()
            service.create(request).fold(
                onSuccess = { track -> call.respond(HttpStatusCode.Created, track) },
                onFailure = { e -> call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message)) }
            )
        }

        // PUT /tracks/{id} - Actualizar track
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val request = call.receive<UpdateTrackRequest>()
            val track = service.update(id, request)

            if (track != null) {
                call.respond(HttpStatusCode.OK, track)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track not found"))
            }
        }

        // DELETE /tracks/{id} - Eliminar track
        delete("/{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "ID is required")
            )

            val deleted = service.delete(id)
            if (deleted) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track not found"))
            }
        }
    }
}
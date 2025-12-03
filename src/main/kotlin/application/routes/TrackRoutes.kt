package com.example.application.routes

import com.example.application.services.TrackService
import com.example.domain.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.trackRoutes() {
    val service by inject<TrackService>()

    route("/tracks") {

        // CREATE - Crear track
        post {
            try {
                val request = call.receive<CreateTrackRequest>()

                if (request.title.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "El título de la canción es requerido")
                    )
                    return@post
                }

                if (request.duration <= 0) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "La duración debe ser mayor a 0")
                    )
                    return@post
                }

                val result = service.createTrack(request)

                result.onSuccess { track ->
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(
                            success = true,
                            message = "Canción creada exitosamente",
                            data = track
                        )
                    )
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = error.message ?: "Error al crear canción")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al crear canción",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener todos los tracks
        get {
            try {
                val tracks = service.getAllTracks()
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        message = "Canciones obtenidas exitosamente",
                        data = tracks
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener canciones",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener track por ID
        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@get
                }

                val track = service.getTrackById(id)
                if (track != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Canción encontrada",
                            data = track
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(message = "Canción no encontrada")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener canción",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener tracks por álbum
        get("/album/{albumId}") {
            try {
                val albumId = call.parameters["albumId"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID del álbum requerido")
                    )
                    return@get
                }

                val tracks = service.getTracksByAlbum(albumId)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        message = "Canciones del álbum obtenidas exitosamente",
                        data = tracks
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener canciones del álbum",
                        error = e.message
                    )
                )
            }
        }

        // UPDATE - Actualizar track
        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@put
                }

                val request = call.receive<UpdateTrackRequest>()

                if (request.duration != null && request.duration <= 0) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "La duración debe ser mayor a 0")
                    )
                    return@put
                }

                val result = service.updateTrack(id, request)

                result.onSuccess { track ->
                    if (track != null) {
                        call.respond(
                            HttpStatusCode.OK,
                            ApiResponse(
                                success = true,
                                message = "Canción actualizada exitosamente",
                                data = track
                            )
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            ErrorResponse(message = "Canción no encontrada")
                        )
                    }
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = error.message ?: "Error al actualizar canción")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al actualizar canción",
                        error = e.message
                    )
                )
            }
        }

        // DELETE - Eliminar track
        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@delete
                }

                val result = service.deleteTrack(id)

                result.onSuccess {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Canción eliminada exitosamente",
                            data = null
                        )
                    )
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(
                            message = error.message ?: "Error al eliminar canción"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al eliminar canción",
                        error = e.message
                    )
                )
            }
        }
    }
}
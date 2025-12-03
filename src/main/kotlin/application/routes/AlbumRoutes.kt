package com.example.application.routes

import com.example.application.services.AlbumService
import com.example.domain.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.albumRoutes(service: AlbumService) {
    route("/albums") {

        // CREATE - Crear álbum
        post {
            try {
                val request = call.receive<CreateAlbumRequest>()

                if (request.title.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "El título del álbum es requerido")
                    )
                    return@post
                }

                if (request.releaseYear < 1900) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "Año de lanzamiento inválido")
                    )
                    return@post
                }

                val result = service.createAlbum(request)

                result.onSuccess { album ->
                    call.respond(
                        HttpStatusCode.Created,
                        ApiResponse(
                            success = true,
                            message = "Álbum creado exitosamente",
                            data = album
                        )
                    )
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = error.message ?: "Error al crear álbum")
                    )
                }
            } catch (e: Exception) {
                call.application.environment.log.error("Error al crear álbum", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al crear álbum",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener todos los álbumes
        get {
            try {
                val albums = service.getAllAlbums()
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        message = "Álbumes obtenidos exitosamente",
                        data = albums
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener álbumes",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener álbum por ID
        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@get
                }

                val album = service.getAlbumById(id)
                if (album != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Álbum encontrado",
                            data = album
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(message = "Álbum no encontrado")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener álbum",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener álbumes por artista
        get("/artista/{artistId}") {
            try {
                val artistId = call.parameters["artistId"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID del artista requerido")
                    )
                    return@get
                }

                val albums = service.getAlbumsByArtist(artistId)
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        message = "Álbumes del artista obtenidos exitosamente",
                        data = albums
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener álbumes del artista",
                        error = e.message
                    )
                )
            }
        }

        // UPDATE - Actualizar álbum
        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@put
                }

                val request = call.receive<UpdateAlbumRequest>()
                val result = service.updateAlbum(id, request)

                result.onSuccess { album ->
                    if (album != null) {
                        call.respond(
                            HttpStatusCode.OK,
                            ApiResponse(
                                success = true,
                                message = "Álbum actualizado exitosamente",
                                data = album
                            )
                        )
                    } else {
                        call.respond(
                            HttpStatusCode.NotFound,
                            ErrorResponse(message = "Álbum no encontrado")
                        )
                    }
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = error.message ?: "Error al actualizar álbum")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al actualizar álbum",
                        error = e.message
                    )
                )
            }
        }

        // DELETE - Eliminar álbum (con protección)
        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@delete
                }

                val result = service.deleteAlbum(id)

                result.onSuccess {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Álbum eliminado exitosamente",
                            data = null
                        )
                    )
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.Conflict,
                        ErrorResponse(
                            message = error.message ?: "Error al eliminar álbum"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al eliminar álbum",
                        error = e.message
                    )
                )
            }
        }
    }
}
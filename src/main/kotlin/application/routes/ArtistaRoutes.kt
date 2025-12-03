package com.example.application.routes

import com.example.application.services.ArtistaService
import com.example.domain.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.artistaRoutes() {
    val service by inject<ArtistaService>()

    route("/artistas") {

        // CREATE - Crear artista
        post {
            try {
                val request = call.receive<CreateArtistaRequest>()

                if (request.name.isBlank()) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "El nombre del artista es requerido")
                    )
                    return@post
                }

                val artista = service.createArtista(request)
                call.respond(
                    HttpStatusCode.Created,
                    ApiResponse(
                        success = true,
                        message = "Artista creado exitosamente",
                        data = artista
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al crear artista",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener todos los artistas
        get {
            try {
                val artistas = service.getAllArtistas()
                call.respond(
                    HttpStatusCode.OK,
                    ApiResponse(
                        success = true,
                        message = "Artistas obtenidos exitosamente",
                        data = artistas
                    )
                )
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener artistas",
                        error = e.message
                    )
                )
            }
        }

        // READ - Obtener artista por ID
        get("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@get
                }

                val artista = service.getArtistaById(id)
                if (artista != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Artista encontrado",
                            data = artista
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(message = "Artista no encontrado")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al obtener artista",
                        error = e.message
                    )
                )
            }
        }

        // UPDATE - Actualizar artista
        put("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@put
                }

                val request = call.receive<UpdateArtistaRequest>()
                val artista = service.updateArtista(id, request)

                if (artista != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Artista actualizado exitosamente",
                            data = artista
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorResponse(message = "Artista no encontrado")
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al actualizar artista",
                        error = e.message
                    )
                )
            }
        }

        // DELETE - Eliminar artista (con protección)
        delete("/{id}") {
            try {
                val id = call.parameters["id"] ?: run {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse(message = "ID requerido")
                    )
                    return@delete
                }

                val result = service.deleteArtista(id)

                result.onSuccess {
                    call.respond(
                        HttpStatusCode.OK,
                        ApiResponse(
                            success = true,
                            message = "Artista eliminado exitosamente",
                            data = null
                        )
                    )
                }.onFailure { error ->
                    call.respond(
                        HttpStatusCode.Conflict,
                        ErrorResponse(
                            message = error.message ?: "Error al eliminar artista"
                        )
                    )
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse(
                        message = "Error al eliminar artista",
                        error = e.message
                    )
                )
            }
        }
    }
}
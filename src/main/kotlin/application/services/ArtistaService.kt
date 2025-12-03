package com.example.application.services

import com.example.domain.models.*
import com.example.domain.ports.*
import com.example.domain.models.Artista
import com.example.domain.models.CreateArtistaRequest
import com.example.domain.models.UpdateArtistaRequest
import com.example.domain.ports.ArtistaRepository

class ArtistaService(private val repository: ArtistaRepository) {

    suspend fun createArtista(request: CreateArtistaRequest): Artista {
        return repository.create(request)
    }

    suspend fun getAllArtistas(): List<Artista> {
        return repository.findAll()
    }

    suspend fun getArtistaById(id: String): Artista? {
        return repository.findById(id)
    }

    suspend fun updateArtista(id: String, request: UpdateArtistaRequest): Artista? {
        return repository.update(id, request)
    }

    suspend fun deleteArtista(id: String): Result<Boolean> {
        // Protección: verificar si tiene álbumes
        if (repository.hasAlbums(id)) {
            return Result.failure(
                Exception("No se puede eliminar el artista porque tiene álbumes asociados")
            )
        }

        val deleted = repository.delete(id)
        return if (deleted) {
            Result.success(true)
        } else {
            Result.failure(Exception("Artista no encontrado"))
        }
    }
}
package com.example.application.services

import com.example.domain.models.Album
import com.example.domain.models.CreateAlbumRequest
import com.example.domain.models.UpdateAlbumRequest
import com.example.domain.ports.AlbumRepository
import com.example.domain.ports.ArtistaRepository

class AlbumService(
    private val repository: AlbumRepository,
    private val artistaRepository: ArtistaRepository
) {

    suspend fun createAlbum(request: CreateAlbumRequest): Result<Album> {
        // Verificar que el artista existe
        val artista = artistaRepository.findById(request.artistId)
            ?: return Result.failure(Exception("El artista especificado no existe"))

        val album = repository.create(request)
        return Result.success(album)
    }

    suspend fun getAllAlbums(): List<Album> {
        return repository.findAll()
    }

    suspend fun getAlbumById(id: String): Album? {
        return repository.findById(id)
    }

    suspend fun getAlbumsByArtist(artistId: String): List<Album> {
        return repository.findByArtistId(artistId)
    }

    suspend fun updateAlbum(id: String, request: UpdateAlbumRequest): Result<Album?> {
        // Si se está cambiando el artista, verificar que existe
        if (request.artistId != null) {
            val artista = artistaRepository.findById(request.artistId)
                ?: return Result.failure(Exception("El artista especificado no existe"))
        }

        val album = repository.update(id, request)
        return Result.success(album)
    }

    suspend fun deleteAlbum(id: String): Result<Boolean> {
        // Protección: verificar si tiene tracks
        if (repository.hasTracks(id)) {
            return Result.failure(
                Exception("No se puede eliminar el álbum porque tiene canciones asociadas")
            )
        }

        val deleted = repository.delete(id)
        return if (deleted) {
            Result.success(true)
        } else {
            Result.failure(Exception("Álbum no encontrado"))
        }
    }
}

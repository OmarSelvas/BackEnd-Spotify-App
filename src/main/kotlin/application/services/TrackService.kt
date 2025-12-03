package com.example.application.services

import com.example.domain.models.CreateTrackRequest
import com.example.domain.models.Track
import com.example.domain.models.UpdateTrackRequest
import com.example.domain.ports.AlbumRepository
import com.example.domain.ports.TrackRepository

class TrackService (
    private val repository: TrackRepository,
    private val albumRepository: AlbumRepository
) {

    suspend fun createTrack(request: CreateTrackRequest): Result<Track> {
        // Verificar que el álbum existe
        val album = albumRepository.findById(request.albumId)
            ?: return Result.failure(Exception("El álbum especificado no existe"))

        val track = repository.create(request)
        return Result.success(track)
    }

    suspend fun getAllTracks(): List<Track> {
        return repository.findAll()
    }

    suspend fun getTrackById(id: String): Track? {
        return repository.findById(id)
    }

    suspend fun getTracksByAlbum(albumId: String): List<Track> {
        return repository.findByAlbumId(albumId)
    }

    suspend fun updateTrack(id: String, request: UpdateTrackRequest): Result<Track?> {
        // Si se está cambiando el álbum, verificar que existe
        if (request.albumId != null) {
            val album = albumRepository.findById(request.albumId)
                ?: return Result.failure(Exception("El álbum especificado no existe"))
        }

        val track = repository.update(id, request)
        return Result.success(track)
    }

    suspend fun deleteTrack(id: String): Result<Boolean> {
        val deleted = repository.delete(id)
        return if (deleted) {
            Result.success(true)
        } else {
            Result.failure(Exception("Track no encontrado"))
        }
    }
}
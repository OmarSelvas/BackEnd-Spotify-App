package com.example.application.services

import com.example.domain.models.*
import com.example.domain.ports.*

class ArtistaService(private val repository: ArtistaRepository) {
    suspend fun getAll() = repository.getAll()
    suspend fun getById(id: String) = repository.getById(id)
    suspend fun create(request: CreateArtistaRequest) = repository.create(request)
    suspend fun update(id: String, request: UpdateArtistaRequest) = repository.update(id, request)
    suspend fun delete(id: String) = repository.delete(id)
    suspend fun getWithAlbums(id: String) = repository.getWithAlbums(id)
}

class AlbumService(private val repository: AlbumRepository) {
    suspend fun getAll() = repository.getAll()
    suspend fun getById(id: String) = repository.getById(id)
    suspend fun getByArtistId(artistId: String) = repository.getByArtistId(artistId)
    suspend fun create(request: CreateAlbumRequest): Result<Album> {
        if (request.releaseYear < 1900) {
            return Result.failure(IllegalArgumentException("Release year must be >= 1900"))
        }
        return Result.success(repository.create(request))
    }
    suspend fun update(id: String, request: UpdateAlbumRequest) = repository.update(id, request)
    suspend fun delete(id: String) = repository.delete(id)
    suspend fun getWithTracks(id: String) = repository.getWithTracks(id)
}

class TrackService(private val repository: TrackRepository) {
    suspend fun getAll() = repository.getAll()
    suspend fun getById(id: String) = repository.getById(id)
    suspend fun getByAlbumId(albumId: String) = repository.getByAlbumId(albumId)
    suspend fun create(request: CreateTrackRequest): Result<Track> {
        if (request.duration <= 0) {
            return Result.failure(IllegalArgumentException("Duration must be > 0"))
        }
        return Result.success(repository.create(request))
    }
    suspend fun update(id: String, request: UpdateTrackRequest) = repository.update(id, request)
    suspend fun delete(id: String) = repository.delete(id)
}
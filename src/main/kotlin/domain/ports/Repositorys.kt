package com.example.domain.ports

import com.example.domain.models.*

// ARTISTA REPOSITORY
interface ArtistaRepository {
    suspend fun create(request: CreateArtistaRequest): Artista
    suspend fun findAll(): List<Artista>
    suspend fun findById(id: String): Artista?
    suspend fun update(id: String, request: UpdateArtistaRequest): Artista?
    suspend fun delete(id: String): Boolean
    suspend fun hasAlbums(id: String): Boolean
}

//  ALBUM REPOSITORY
interface AlbumRepository {
    suspend fun create(request: CreateAlbumRequest): Album
    suspend fun findAll(): List<Album>
    suspend fun findById(id: String): Album?
    suspend fun findByArtistId(artistId: String): List<Album>
    suspend fun update(id: String, request: UpdateAlbumRequest): Album?
    suspend fun delete(id: String): Boolean
    suspend fun hasTracks(id: String): Boolean
}

// TRACK REPOSITORY =
interface TrackRepository {
    suspend fun create(request: CreateTrackRequest): Track
    suspend fun findAll(): List<Track>
    suspend fun findById(id: String): Track?
    suspend fun findByAlbumId(albumId: String): List<Track>
    suspend fun update(id: String, request: UpdateTrackRequest): Track?
    suspend fun delete(id: String): Boolean
}
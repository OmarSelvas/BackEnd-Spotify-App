package com.example.domain.ports

import com.example.domain.models.*

// ========== ARTISTA REPOSITORY ==========
interface ArtistaRepository {
    suspend fun getAll(): List<Artista>
    suspend fun getById(id: String): Artista?
    suspend fun create(request: CreateArtistaRequest): Artista
    suspend fun update(id: String, request: UpdateArtistaRequest): Artista?
    suspend fun delete(id: String): Boolean
    suspend fun getWithAlbums(id: String): ArtistaWithAlbums?
}

// ========== ALBUM REPOSITORY ==========
interface AlbumRepository {
    suspend fun getAll(): List<Album>
    suspend fun getById(id: String): Album?
    suspend fun getByArtistId(artistId: String): List<Album>
    suspend fun create(request: CreateAlbumRequest): Album
    suspend fun update(id: String, request: UpdateAlbumRequest): Album?
    suspend fun delete(id: String): Boolean
    suspend fun getWithTracks(id: String): AlbumWithTracks?
}

// ========== TRACK REPOSITORY ==========
interface TrackRepository {
    suspend fun getAll(): List<Track>
    suspend fun getById(id: String): Track?
    suspend fun getByAlbumId(albumId: String): List<Track>
    suspend fun create(request: CreateTrackRequest): Track
    suspend fun update(id: String, request: UpdateTrackRequest): Track?
    suspend fun delete(id: String): Boolean
}
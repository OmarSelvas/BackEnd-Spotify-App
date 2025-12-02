package com.example.domain.models

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

// ========== ARTISTA ==========
@Serializable
data class Artista(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val genre: String? = null,
    val createdAt: String = Instant.now().toString(),
    val updatedAt: String = Instant.now().toString()
)

@Serializable
data class CreateArtistaRequest(
    val name: String,
    val genre: String? = null
)

@Serializable
data class UpdateArtistaRequest(
    val name: String? = null,
    val genre: String? = null
)

// ========== ALBUM ==========
@Serializable
data class Album(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val releaseYear: Int,
    val artistId: String,
    val createdAt: String = Instant.now().toString(),
    val updatedAt: String = Instant.now().toString()
)

@Serializable
data class CreateAlbumRequest(
    val title: String,
    val releaseYear: Int,
    val artistId: String
)

@Serializable
data class UpdateAlbumRequest(
    val title: String? = null,
    val releaseYear: Int? = null
)

// ========== TRACK ==========
@Serializable
data class Track(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val duration: Int, // en segundos
    val albumId: String,
    val createdAt: String = Instant.now().toString(),
    val updatedAt: String = Instant.now().toString()
)

@Serializable
data class CreateTrackRequest(
    val title: String,
    val duration: Int,
    val albumId: String
)

@Serializable
data class UpdateTrackRequest(
    val title: String? = null,
    val duration: Int? = null
)

// ========== RESPONSES CON RELACIONES ==========
@Serializable
data class ArtistaWithAlbums(
    val artista: Artista,
    val albums: List<Album>
)

@Serializable
data class AlbumWithTracks(
    val album: Album,
    val tracks: List<Track>
)
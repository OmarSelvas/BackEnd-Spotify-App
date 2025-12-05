package com.example.domain.models

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

// ARTISTA
@Serializable
data class Artista(
    val id: String,
    val name: String,
    val genre: String?,
    val createdAt: String,
    val updatedAt: String
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

// ALBUM
@Serializable
data class Album(
    val id: String,
    val title: String,
    val releaseYear: Int,
    val artistId: String,
    val artistName: String? = null,
    val createdAt: String,
    val updatedAt: String
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
    val releaseYear: Int? = null,
    val artistId: String? = null
)

// TRACK
@Serializable
data class Track(
    val id: String,
    val title: String,
    val duration: Int,
    val albumId: String,
    val albumTitle: String? = null,
    val artistName: String? = null,
    val createdAt: String,
    val updatedAt: String
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
    val duration: Int? = null,
    val albumId: String? = null
)

// Respuestas
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null
)

@Serializable
data class ErrorResponse(
    val success: Boolean = false,
    val message: String,
    val error: String? = null
)
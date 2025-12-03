package com.example.domain.models

import kotlinx.serialization.Serializable

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

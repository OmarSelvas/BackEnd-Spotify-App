package com.example.domain.models

import kotlinx.serialization.Serializable

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
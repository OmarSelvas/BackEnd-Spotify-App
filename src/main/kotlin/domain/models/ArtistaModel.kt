package com.example.domain.models

import kotlinx.serialization.Serializable

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

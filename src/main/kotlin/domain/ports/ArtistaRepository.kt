package com.example.domain.ports

import com.example.domain.models.Artista
import com.example.domain.models.CreateArtistaRequest
import com.example.domain.models.UpdateArtistaRequest

interface ArtistaRepository  {
    suspend fun create(request: CreateArtistaRequest): Artista
    suspend fun findAll(): List<Artista>
    suspend fun findById(id: String): Artista?
    suspend fun update(id: String, request: UpdateArtistaRequest): Artista?
    suspend fun delete(id: String): Boolean
    suspend fun hasAlbums(id: String): Boolean
}
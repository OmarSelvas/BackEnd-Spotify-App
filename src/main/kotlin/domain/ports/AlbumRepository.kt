package com.example.domain.ports

import com.example.domain.models.Album
import com.example.domain.models.CreateAlbumRequest
import com.example.domain.models.UpdateAlbumRequest

interface AlbumRepository {
    suspend fun create(request: CreateAlbumRequest): Album
    suspend fun findAll(): List<Album>
    suspend fun findById(id: String): Album?
    suspend fun findByArtistId(artistId: String): List<Album>
    suspend fun update(id: String, request: UpdateAlbumRequest): Album?
    suspend fun delete(id: String): Boolean
    suspend fun hasTracks(id: String): Boolean
}
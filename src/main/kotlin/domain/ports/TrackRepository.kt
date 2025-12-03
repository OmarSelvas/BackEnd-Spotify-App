package com.example.domain.ports

import com.example.domain.models.CreateTrackRequest
import com.example.domain.models.Track
import com.example.domain.models.UpdateTrackRequest

interface TrackRepository {
    suspend fun create(request: CreateTrackRequest): Track
    suspend fun findAll(): List<Track>
    suspend fun findById(id: String): Track?
    suspend fun findByAlbumId(albumId: String): List<Track>
    suspend fun update(id: String, request: UpdateTrackRequest): Track?
    suspend fun delete(id: String): Boolean
}
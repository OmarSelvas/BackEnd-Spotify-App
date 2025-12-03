package com.example.infrastructure.adapters

import com.example.domain.models.*
import com.example.domain.ports.TrackRepository
import com.example.infrastructure.DatabaseFactory.dbQuery
import com.example.infrastructure.schemas.AlbumesTable
import com.example.infrastructure.schemas.ArtistasTable
import com.example.infrastructure.schemas.TracksTable
import org.jetbrains.exposed.sql.*
import java.time.Instant
import java.util.UUID

class TrackRepositoryImpl : TrackRepository {

    private fun ResultRow.toTrack() = Track(
        id = this[TracksTable.id].toString(),
        title = this[TracksTable.title],
        duration = this[TracksTable.duration],
        albumId = this[TracksTable.albumId].toString(),
        albumTitle = this.getOrNull(AlbumesTable.title),
        artistName = this.getOrNull(ArtistasTable.name),
        createdAt = this[TracksTable.createdAt].toString(),
        updatedAt = this[TracksTable.updatedAt].toString()
    )

    override suspend fun create(request: CreateTrackRequest): Track = dbQuery {
        val insertStatement = TracksTable.insert {
            it[title] = request.title
            it[duration] = request.duration
            it[albumId] = UUID.fromString(request.albumId)
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }

        val trackId = insertStatement.resultedValues!!.first()[TracksTable.id]

        // Obtener con JOINs para incluir datos del álbum y artista
        (TracksTable innerJoin AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .where { TracksTable.id eq trackId }
            .map { it.toTrack() }
            .first()
    }

    override suspend fun findAll(): List<Track> = dbQuery {
        (TracksTable innerJoin AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .map { it.toTrack() }
    }

    override suspend fun findById(id: String): Track? = dbQuery {
        (TracksTable innerJoin AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .where { TracksTable.id eq UUID.fromString(id) }
            .map { it.toTrack() }
            .singleOrNull()
    }

    override suspend fun findByAlbumId(albumId: String): List<Track> = dbQuery {
        (TracksTable innerJoin AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .where { TracksTable.albumId eq UUID.fromString(albumId) }
            .map { it.toTrack() }
    }

    override suspend fun update(id: String, request: UpdateTrackRequest): Track? = dbQuery {
        val uuid = UUID.fromString(id)

        val updated = TracksTable.update({ TracksTable.id eq uuid }) {
            request.title?.let { title -> it[TracksTable.title] = title }
            request.duration?.let { duration -> it[TracksTable.duration] = duration }
            request.albumId?.let { albumId -> it[TracksTable.albumId] = UUID.fromString(albumId) }
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) findById(id) else null
    }

    override suspend fun delete(id: String): Boolean = dbQuery {
        val uuid = UUID.fromString(id)
        TracksTable.deleteWhere { TracksTable.id eq uuid } > 0
    }
}
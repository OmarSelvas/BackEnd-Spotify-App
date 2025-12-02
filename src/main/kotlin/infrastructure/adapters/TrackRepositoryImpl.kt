package com.example.infrastructure.adapters

import com.example.domain.models.*
import com.example.domain.ports.TrackRepository
import com.example.infrastructure.schemas.TracksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.UUID

class TrackRepositoryImpl : TrackRepository {

    override suspend fun getAll(): List<Track> = newSuspendedTransaction {
        TracksTable.selectAll().map { toTrack(it) }
    }

    override suspend fun getById(id: String): Track? = newSuspendedTransaction {
        TracksTable.select { TracksTable.id eq UUID.fromString(id) }
            .mapNotNull { toTrack(it) }
            .singleOrNull()
    }

    override suspend fun getByAlbumId(albumId: String): List<Track> = newSuspendedTransaction {
        TracksTable.select { TracksTable.albumId eq UUID.fromString(albumId) }
            .map { toTrack(it) }
    }

    override suspend fun create(request: CreateTrackRequest): Track = newSuspendedTransaction {
        val newId = TracksTable.insert {
            it[title] = request.title
            it[duration] = request.duration
            it[albumId] = UUID.fromString(request.albumId)
        } get TracksTable.id

        Track(
            id = newId.toString(),
            title = request.title,
            duration = request.duration,
            albumId = request.albumId,
            createdAt = Instant.now().toString(),
            updatedAt = Instant.now().toString()
        )
    }

    override suspend fun update(id: String, request: UpdateTrackRequest): Track? = newSuspendedTransaction {
        val uuid = UUID.fromString(id)
        val updated = TracksTable.update({ TracksTable.id eq uuid }) {
            request.title?.let { title -> it[TracksTable.title] = title }
            request.duration?.let { duration -> it[TracksTable.duration] = duration }
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) {
            TracksTable.select { TracksTable.id eq uuid }
                .mapNotNull { toTrack(it) }
                .singleOrNull()
        } else null
    }

    override suspend fun delete(id: String): Boolean = newSuspendedTransaction {
        TracksTable.deleteWhere { TracksTable.id eq UUID.fromString(id) } > 0
    }

    private fun toTrack(row: ResultRow): Track = Track(
        id = row[TracksTable.id].toString(),
        title = row[TracksTable.title],
        duration = row[TracksTable.duration],
        albumId = row[TracksTable.albumId].toString(),
        createdAt = row[TracksTable.createdAt].toString(),
        updatedAt = row[TracksTable.updatedAt].toString()
    )
}
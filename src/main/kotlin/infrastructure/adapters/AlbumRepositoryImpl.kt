package com.example.infrastructure.adapters

import com.example.domain.models.*
import com.example.domain.ports.AlbumRepository
import com.example.infrastructure.schemas.AlbumesTable
import com.example.infrastructure.schemas.TracksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.UUID

class AlbumRepositoryImpl : AlbumRepository {

    override suspend fun getAll(): List<Album> = newSuspendedTransaction {
        AlbumesTable.selectAll().map { toAlbum(it) }
    }

    override suspend fun getById(id: String): Album? = newSuspendedTransaction {
        AlbumesTable.select { AlbumesTable.id eq UUID.fromString(id) }
            .mapNotNull { toAlbum(it) }
            .singleOrNull()
    }

    override suspend fun getByArtistId(artistId: String): List<Album> = newSuspendedTransaction {
        AlbumesTable.select { AlbumesTable.artistId eq UUID.fromString(artistId) }
            .map { toAlbum(it) }
    }

    override suspend fun create(request: CreateAlbumRequest): Album = newSuspendedTransaction {
        val newId = AlbumesTable.insert {
            it[title] = request.title
            it[releaseYear] = request.releaseYear
            it[artistId] = UUID.fromString(request.artistId)
        } get AlbumesTable.id

        Album(
            id = newId.toString(),
            title = request.title,
            releaseYear = request.releaseYear,
            artistId = request.artistId,
            createdAt = Instant.now().toString(),
            updatedAt = Instant.now().toString()
        )
    }

    override suspend fun update(id: String, request: UpdateAlbumRequest): Album? = newSuspendedTransaction {
        val uuid = UUID.fromString(id)
        val updated = AlbumesTable.update({ AlbumesTable.id eq uuid }) {
            request.title?.let { title -> it[AlbumesTable.title] = title }
            request.releaseYear?.let { year -> it[releaseYear] = year }
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) {
            AlbumesTable.select { AlbumesTable.id eq uuid }
                .mapNotNull { toAlbum(it) }
                .singleOrNull()
        } else null
    }

    override suspend fun delete(id: String): Boolean = newSuspendedTransaction {
        AlbumesTable.deleteWhere { AlbumesTable.id eq UUID.fromString(id) } > 0
    }

    override suspend fun getWithTracks(id: String): AlbumWithTracks? = newSuspendedTransaction {
        val albumRow = AlbumesTable.select { AlbumesTable.id eq UUID.fromString(id) }
            .singleOrNull() ?: return@newSuspendedTransaction null

        val album = toAlbum(albumRow)

        val tracks = TracksTable.select { TracksTable.albumId eq UUID.fromString(id) }
            .map { row ->
                Track(
                    id = row[TracksTable.id].toString(),
                    title = row[TracksTable.title],
                    duration = row[TracksTable.duration],
                    albumId = row[TracksTable.albumId].toString(),
                    createdAt = row[TracksTable.createdAt].toString(),
                    updatedAt = row[TracksTable.updatedAt].toString()
                )
            }

        AlbumWithTracks(album, tracks)
    }

    private fun toAlbum(row: ResultRow): Album = Album(
        id = row[AlbumesTable.id].toString(),
        title = row[AlbumesTable.title],
        releaseYear = row[AlbumesTable.releaseYear],
        artistId = row[AlbumesTable.artistId].toString(),
        createdAt = row[AlbumesTable.createdAt].toString(),
        updatedAt = row[AlbumesTable.updatedAt].toString()
    )
}
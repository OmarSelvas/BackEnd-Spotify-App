package com.example.infrastructure.adapters

import com.example.domain.models.*
import com.example.domain.ports.AlbumRepository
import com.example.infrastructure.DatabaseFactory.dbQuery
import com.example.infrastructure.schemas.AlbumesTable
import com.example.infrastructure.schemas.ArtistasTable
import com.example.infrastructure.schemas.TracksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.Instant
import java.util.UUID

class AlbumRepositoryImpl : AlbumRepository {

    private fun ResultRow.toAlbum() = Album(
        id = this[AlbumesTable.id].toString(),
        title = this[AlbumesTable.title],
        releaseYear = this[AlbumesTable.releaseYear],
        artistId = this[AlbumesTable.artistId].toString(),
        artistName = this.getOrNull(ArtistasTable.name),
        createdAt = this[AlbumesTable.createdAt].toString(),
        updatedAt = this[AlbumesTable.updatedAt].toString()
    )

    override suspend fun create(request: CreateAlbumRequest): Album = dbQuery {
        val insertStatement = AlbumesTable.insert {
            it[title] = request.title
            it[releaseYear] = request.releaseYear
            it[artistId] = UUID.fromString(request.artistId)
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }

        val albumId = insertStatement.resultedValues!!.first()[AlbumesTable.id]

        // Obtener con JOIN para incluir nombre del artista
        (AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .where { AlbumesTable.id eq albumId }
            .map { it.toAlbum() }
            .first()
    }

    override suspend fun findAll(): List<Album> = dbQuery {
        (AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .map { it.toAlbum() }
    }

    override suspend fun findById(id: String): Album? = dbQuery {
        (AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .where { AlbumesTable.id eq UUID.fromString(id) }
            .map { it.toAlbum() }
            .singleOrNull()
    }

    override suspend fun findByArtistId(artistId: String): List<Album> = dbQuery {
        (AlbumesTable innerJoin ArtistasTable)
            .selectAll()
            .where { AlbumesTable.artistId eq UUID.fromString(artistId) }
            .map { it.toAlbum() }
    }

    override suspend fun update(id: String, request: UpdateAlbumRequest): Album? = dbQuery {
        val uuid = UUID.fromString(id)

        val updated = AlbumesTable.update({ AlbumesTable.id eq uuid }) {
            request.title?.let { title -> it[AlbumesTable.title] = title }
            request.releaseYear?.let { year -> it[releaseYear] = year }
            request.artistId?.let { artistId -> it[AlbumesTable.artistId] = UUID.fromString(artistId) }
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) findById(id) else null
    }

    override suspend fun delete(id: String): Boolean = dbQuery {
        val uuid = UUID.fromString(id)
        AlbumesTable.deleteWhere { AlbumesTable.id eq uuid } > 0
    }

    override suspend fun hasTracks(id: String): Boolean = dbQuery {
        val uuid = UUID.fromString(id)
        TracksTable.selectAll()
            .where { TracksTable.albumId eq uuid }
            .count() > 0
    }
}
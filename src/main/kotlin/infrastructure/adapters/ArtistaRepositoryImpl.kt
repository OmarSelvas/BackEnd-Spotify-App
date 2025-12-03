package com.example.infrastructure.adapters

import com.example.domain.models.*
import com.example.domain.ports.ArtistaRepository
import com.example.infrastructure.DatabaseFactory.dbQuery
import com.example.infrastructure.schemas.AlbumesTable
import com.example.infrastructure.schemas.ArtistasTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.time.Instant
import java.util.UUID

class ArtistaRepositoryImpl : ArtistaRepository {

    private fun ResultRow.toArtista() = Artista(
        id = this[ArtistasTable.id].toString(),
        name = this[ArtistasTable.name],
        genre = this[ArtistasTable.genre],
        createdAt = this[ArtistasTable.createdAt].toString(),
        updatedAt = this[ArtistasTable.updatedAt].toString()
    )

    override suspend fun create(request: CreateArtistaRequest): Artista = dbQuery {
        val insertStatement = ArtistasTable.insert {
            it[name] = request.name
            it[genre] = request.genre
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }
        insertStatement.resultedValues!!.first().toArtista()
    }

    override suspend fun findAll(): List<Artista> = dbQuery {
        ArtistasTable.selectAll().map { it.toArtista() }
    }

    override suspend fun findById(id: String): Artista? = dbQuery {
        ArtistasTable.selectAll()
            .where { ArtistasTable.id eq UUID.fromString(id) }
            .map { it.toArtista() }
            .singleOrNull()
    }

    override suspend fun update(id: String, request: UpdateArtistaRequest): Artista? = dbQuery {
        val uuid = UUID.fromString(id)

        val updated = ArtistasTable.update({ ArtistasTable.id eq uuid }) {
            request.name?.let { name -> it[ArtistasTable.name] = name }
            request.genre?.let { genre -> it[ArtistasTable.genre] = genre }
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) findById(id) else null
    }

    override suspend fun delete(id: String): Boolean = dbQuery {
        val uuid = UUID.fromString(id)
        ArtistasTable.deleteWhere { ArtistasTable.id eq uuid } > 0
    }

    override suspend fun hasAlbums(id: String): Boolean = dbQuery {
        val uuid = UUID.fromString(id)
        AlbumesTable.selectAll()
            .where { AlbumesTable.artistId eq uuid }
            .count() > 0
    }
}
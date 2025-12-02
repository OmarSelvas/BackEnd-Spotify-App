package com.example.infrastructure.adapters

import com.example.domain.models.*
import com.example.domain.ports.ArtistaRepository
import com.example.infrastructure.schemas.ArtistasTable
import com.example.infrastructure.schemas.AlbumesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class ArtistaRepositoryImpl : ArtistaRepository {

    override suspend fun getAll(): List<Artista> = transaction {
        ArtistasTable.selectAll().map { toArtista(it) }
    }

    override suspend fun getById(id: String): Artista? = transaction {
        ArtistasTable.select { ArtistasTable.id eq UUID.fromString(id) }
            .mapNotNull { toArtista(it) }
            .singleOrNull()
    }

    override suspend fun create(request: CreateArtistaRequest): Artista = transaction {
        val newId = ArtistasTable.insert {
            it[name] = request.name
            it[genre] = request.genre
        } get ArtistasTable.id

        Artista(
            id = newId.toString(),
            name = request.name,
            genre = request.genre,
            createdAt = Instant.now().toString(),
            updatedAt = Instant.now().toString()
        )
    }

    override suspend fun update(id: String, request: UpdateArtistaRequest): Artista? = transaction {
        val uuid = UUID.fromString(id)
        val updated = ArtistasTable.update({ ArtistasTable.id eq uuid }) {
            request.name?.let { name -> it[ArtistasTable.name] = name }
            request.genre?.let { genre -> it[ArtistasTable.genre] = genre }
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) getById(id) else null
    }

    override suspend fun delete(id: String): Boolean = transaction {
        ArtistasTable.deleteWhere { ArtistasTable.id eq UUID.fromString(id) } > 0
    }

    override suspend fun getWithAlbums(id: String): ArtistaWithAlbums? = transaction {
        val artista = getById(id) ?: return@transaction null

        val albums = (ArtistasTable innerJoin AlbumesTable)
            .select { ArtistasTable.id eq UUID.fromString(id) }
            .map { row ->
                Album(
                    id = row[AlbumesTable.id].toString(),
                    title = row[AlbumesTable.title],
                    releaseYear = row[AlbumesTable.releaseYear],
                    artistId = row[AlbumesTable.artistId].toString(),
                    createdAt = row[AlbumesTable.createdAt].toString(),
                    updatedAt = row[AlbumesTable.updatedAt].toString()
                )
            }

        ArtistaWithAlbums(artista, albums)
    }

    private fun toArtista(row: ResultRow): Artista = Artista(
        id = row[ArtistasTable.id].toString(),
        name = row[ArtistasTable.name],
        genre = row[ArtistasTable.genre],
        createdAt = row[ArtistasTable.createdAt].toString(),
        updatedAt = row[ArtistasTable.updatedAt].toString()
    )
}
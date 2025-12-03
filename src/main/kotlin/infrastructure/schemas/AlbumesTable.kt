package com.example.infrastructure.schemas

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object AlbumesTable : Table("albumes") {
    val id = uuid("id").autoGenerate()
    val title = varchar("title", 150)
    val releaseYear = integer("release_year")
    val artistId = uuid("artist_id").references(ArtistasTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())

    override val primaryKey = PrimaryKey(id)
}
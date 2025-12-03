package com.example.infrastructure.schemas

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object TracksTable : Table("tracks") {
    val id = uuid("id").autoGenerate()
    val title = varchar("title", 150)
    val duration = integer("duration")
    val albumId = uuid("album_id").references(AlbumesTable.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").default(Instant.now())
    val updatedAt = timestamp("updated_at").default(Instant.now())

    override val primaryKey = PrimaryKey(id)
}
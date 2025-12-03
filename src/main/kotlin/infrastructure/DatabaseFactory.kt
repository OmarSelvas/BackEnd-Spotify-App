package com.example.infrastructure

import com.example.infrastructure.schemas.*
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    fun init() {
        val driverClassName = "org.postgresql.Driver"
        val jdbcURL = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/music_db"
        val dbUser = System.getenv("DATABASE_USER") ?: "postgres"
        val dbPassword = System.getenv("DATABASE_PASSWORD") ?: "omar"

        val config = HikariConfig().apply {
            this.driverClassName = driverClassName
            this.jdbcUrl = jdbcURL
            this.username = dbUser
            this.password = dbPassword
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        // Crear tablas si no existen
        transaction {
            SchemaUtils.create(ArtistasTable, AlbumesTable, TracksTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
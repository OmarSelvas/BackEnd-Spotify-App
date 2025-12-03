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
        val jdbcURL = "jdbc:postgresql://localhost:5432/music_db"
        val dbUser = "postgres"
        val dbPassword = "omar"

        println("🔌 Conectando a base de datos: $jdbcURL")

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

        try {
            val dataSource = HikariDataSource(config)
            Database.connect(dataSource)

            println("✅ Conexión a base de datos establecida")

            // Verificar y crear extensión UUID
            transaction {
                try {
                    exec("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";")
                    println("✅ Extensión uuid-ossp habilitada")
                } catch (e: Exception) {
                    println("⚠️ La extensión uuid-ossp ya existe o no es necesaria")
                }

                // Crear tablas si no existen
                SchemaUtils.createMissingTablesAndColumns(ArtistasTable, AlbumesTable, TracksTable)
                println("✅ Tablas verificadas/creadas correctamente")
            }

            println("✅ Base de datos inicializada correctamente")
        } catch (e: Exception) {
            println("❌ Error al conectar con la base de datos: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
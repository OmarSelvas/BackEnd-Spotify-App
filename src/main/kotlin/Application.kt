package com.example

import com.example.infrastructure.DatabaseFactory
import com.example.infrastructure.plugins.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Configurar Koin para inyección de dependencias
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    // Inicializar base de datos
    DatabaseFactory.init()

    // Configurar serialización JSON
    configureSerialization()

    // Configurar rutas
    configureRouting()
}
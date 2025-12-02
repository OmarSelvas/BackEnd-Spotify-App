package com.example

import com.example.infrastructure.DatabaseFactory
import com.example.infrastructure.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Inicializar base de datos
    DatabaseFactory.init()

    // Configurar plugins
    configureDI()
    configureSerialization()
    configureStatusPages()
    configureApiRouting()

    // Log de inicio
    environment.log.info("Music API - Ktor con Arquitectura Hexagonal wuuuuu ")
}
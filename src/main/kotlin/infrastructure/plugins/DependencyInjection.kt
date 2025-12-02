package com.example.infrastructure.plugins

import com.example.application.services.*
import com.example.domain.ports.*
import com.example.infrastructure.adapters.*
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    // Repositories
    single<ArtistaRepository> { ArtistaRepositoryImpl() }
    single<AlbumRepository> { AlbumRepositoryImpl() }
    single<TrackRepository> { TrackRepositoryImpl() }

    // Services
    single { ArtistaService(get()) }
    single { AlbumService(get()) }
    single { TrackService(get()) }
}

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}
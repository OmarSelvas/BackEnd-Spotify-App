package com.example.infrastructure.plugins

import com.example.application.services.*
import com.example.domain.ports.*
import com.example.infrastructure.adapters.*
import org.koin.dsl.module

val appModule = module {

    // REPOSITORIES
    single<ArtistaRepository> { ArtistaRepositoryImpl() }
    single<AlbumRepository> { AlbumRepositoryImpl() }
    single<TrackRepository> { TrackRepositoryImpl() }

    // SERVICES
    single { ArtistaService(get()) }
    single { AlbumService(get(), get()) }
    single { TrackService(get(), get()) }
}
package com.edgar.googletranslatejetpackcompose.di

import com.edgar.googletranslatejetpackcompose.data.repository.TranslationRepository
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
}

val repositoryModule = module {
    single { TranslationRepository(get()) }
}

val viewModelModule = module {
    viewModel { TranslationViewModel(get()) }
}

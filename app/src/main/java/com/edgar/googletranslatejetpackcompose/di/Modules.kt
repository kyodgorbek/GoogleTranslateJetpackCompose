package com.edgar.googletranslatejetpackcompose.di

import com.edgar.googletranslatejetpackcompose.core.data.networking.HttpClientFactory
import com.edgar.googletranslatejetpackcompose.core.mappers.LanguageMapper
import com.edgar.googletranslatejetpackcompose.data.remote.repository.TranslationRepository
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create(CIO.create()) } // Provides HttpClient
}

val mapperModule = module {
    single { LanguageMapper() } // Provides LanguageMapper
}

val repositoryModule = module {
    single { TranslationRepository(get(), get()) } // Inject HttpClient and LanguageMapper
}

val viewModelModule = module {
    viewModel { TranslationViewModel(get()) } // Inject TranslationRepository into ViewModel
}
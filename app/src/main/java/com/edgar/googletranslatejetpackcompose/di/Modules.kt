package com.edgar.googletranslatejetpackcompose.di

import com.edgar.googletranslatejetpackcompose.core.data.networking.HttpClientFactory
import com.edgar.googletranslatejetpackcompose.data.remote.repository.TranslationRepository
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel
import io.ktor.client.engine.cio.CIO
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { HttpClientFactory.create(CIO.create()) }
}

val repositoryModule = module {
    single { TranslationRepository(get()) }
}

val viewModelModule = module {
    viewModel { TranslationViewModel(get()) }
}

package com.edgar.googletranslatejetpackcompose.data.remote.repository

import android.util.Log
import com.edgar.googletranslatejetpackcompose.BuildConfig
import com.edgar.googletranslatejetpackcompose.core.data.networking.constructUrl
import com.edgar.googletranslatejetpackcompose.core.data.networking.safaCall
import com.edgar.googletranslatejetpackcompose.domain.Language
import com.edgar.googletranslatejetpackcompose.core.domain.util.NetworkError
import com.edgar.googletranslatejetpackcompose.core.domain.util.Result
import com.edgar.googletranslatejetpackcompose.core.domain.util.map
import com.edgar.googletranslatejetpackcompose.core.mappers.LanguageMapper
import com.edgar.googletranslatejetpackcompose.data.remote.SupportedLanguagesResponse
import com.edgar.googletranslatejetpackcompose.data.remote.TranslateRequest
import com.edgar.googletranslatejetpackcompose.data.remote.TranslateResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType


open class TranslationRepository(
    private val client: HttpClient,
    private val languageMapper: LanguageMapper
) {

    open suspend fun getSupportedLanguages(): Result<List<Language>, NetworkError> {
        return safaCall<SupportedLanguagesResponse> {
            client.get(constructUrl(BuildConfig.LANGUAGES_API_ENDPOINT)) {
                parameter("key", BuildConfig.API_KEY)
            }
        }.map { response ->
            response.data?.languageResponses?.mapNotNull { languageResponse ->
                languageMapper.mapToDomain(languageResponse)
            } ?: emptyList()
        }
    }

    open suspend fun translateText(request: TranslateRequest): Result<String, NetworkError> {
        return safaCall<TranslateResponse> {
            client.post(constructUrl(BuildConfig.TRANSLATION_API_ENDPOINT)) {
                parameter("key", BuildConfig.API_KEY)
                contentType(ContentType.Application.Json)
                setBody(request)
                Log.d(
                    "API_REQUEST",
                    "URL: ${constructUrl(BuildConfig.TRANSLATION_API_ENDPOINT)}?key=[REDACTED]"
                )
            }
        }.map { response ->
            response.data?.translations?.firstOrNull()?.translatedText
                ?: "Translation error: No translation found"
        }
    }
}

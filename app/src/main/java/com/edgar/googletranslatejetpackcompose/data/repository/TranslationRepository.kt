package com.edgar.googletranslatejetpackcompose.data.repository

import com.edgar.googletranslatejetpackcompose.BuildConfig
import com.edgar.googletranslatejetpackcompose.data.Language
import com.edgar.googletranslatejetpackcompose.data.SupportedLanguagesResponse
import com.edgar.googletranslatejetpackcompose.data.TranslateRequest
import com.edgar.googletranslatejetpackcompose.data.TranslateResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
class TranslationRepository(private val client: HttpClient) {
    private val apiKey = BuildConfig.API_KEY
    private val languagesApiEndpoint = BuildConfig.LANGUAGES_API_ENDPOINT
    private val translationApiEndpoint = BuildConfig.TRANSLATION_API_ENDPOINT

    suspend fun getSupportedLanguages(): List<Language> {
        try {
            val response: SupportedLanguagesResponse = client.get(languagesApiEndpoint) {
                parameter("key", apiKey)
            }.body()
            return response.languages.map { Language(it.language, it.language) }
        } catch (e: Exception) {
            throw Exception("Error fetching languages: ${e.message}")
        }
    }

    suspend fun translateText(request: TranslateRequest): String {
        try {
            val response: TranslateResponse = client.post(translationApiEndpoint) {
                parameter("key", apiKey)
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
            return response.data?.translations?.firstOrNull()?.translatedText ?: "Translation error"
        } catch (e: Exception) {
            return "Translation failed: ${e.message}"
        }
    }
}
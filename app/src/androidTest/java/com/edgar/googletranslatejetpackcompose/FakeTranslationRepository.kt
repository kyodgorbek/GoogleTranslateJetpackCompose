package com.edgar.googletranslatejetpackcompose



import com.edgar.googletranslatejetpackcompose.core.domain.util.NetworkError
import com.edgar.googletranslatejetpackcompose.core.domain.util.NetworkError.UNKNOWN
import com.edgar.googletranslatejetpackcompose.core.mappers.LanguageMapper
import com.edgar.googletranslatejetpackcompose.data.remote.TranslateRequest
import com.edgar.googletranslatejetpackcompose.data.remote.repository.TranslationRepository
import com.edgar.googletranslatejetpackcompose.domain.Language
import com.edgar.googletranslatejetpackcompose.core.domain.util.Result
import io.ktor.client.HttpClient

class FakeTranslationRepository(
    client: HttpClient,
    languageMapper: LanguageMapper = LanguageMapper()
) : TranslationRepository(client, languageMapper) {

    // Flag to simulate error handling
    var shouldReturnError = false

    // Mock translated text
    var translatedText: String = ""

    // Simulate fetching supported languages
    override suspend fun getSupportedLanguages(): Result<List<Language>, NetworkError> {
        return if (shouldReturnError) {
            Result.Error(NetworkError.UNKNOWN) // Simulate an error
        } else {
            Result.Success(
                listOf(
                    Language("en", "English"),
                    Language("fr", "French")
                ) // Return mock languages
            )
        }
    }

    // Simulate translating text
    override suspend fun translateText(request: TranslateRequest): Result<String, NetworkError> {
        return if (shouldReturnError) {
            Result.Error(NetworkError.UNKNOWN) // Simulate an error
        } else {
            // Return the mock translated text
            Result.Success(translatedText)
        }
    }
}
package com.edgar.googletranslatejetpackcompose.presentation

import com.edgar.googletranslatejetpackcompose.data.remote.repository.TranslationRepository



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edgar.googletranslatejetpackcompose.core.domain.util.Result
import com.edgar.googletranslatejetpackcompose.data.remote.Language
import com.edgar.googletranslatejetpackcompose.data.remote.TranslateRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TranslationViewModel(private val repository: TranslationRepository) : ViewModel() {

    // Source language
    private val _sourceLanguage = MutableStateFlow(Language("en", "English"))
    val sourceLanguage: StateFlow<Language> = _sourceLanguage

    // Target language
    private val _targetLanguage = MutableStateFlow(Language("fr", "French"))
    val targetLanguage: StateFlow<Language> = _targetLanguage

    // Translated text result
    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> = _translatedText

    // List of supported languages
    private val _supportedLanguages = MutableStateFlow<List<Language>>(emptyList())
    val supportedLanguages: StateFlow<List<Language>> = _supportedLanguages

    // Error message state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadSupportedLanguages()
    }

    // Load supported languages
    private fun loadSupportedLanguages() {
        viewModelScope.launch {
            when (val result = repository.getSupportedLanguages()) {
                is Result.Success -> {
                    // Set supported languages list when successful
                    _supportedLanguages.value = result.data
                }
                is Result.Error -> {
                    // Set error message on failure
                    _error.value = "Failed to load languages: ${result.error.name}"
                }
            }
        }
    }

    // Set source language
    fun setSourceLanguage(language: Language) {
        _sourceLanguage.value = language
    }

    // Set target language
    fun setTargetLanguage(language: Language) {
        _targetLanguage.value = language
    }

    // Swap source and target languages
    fun swapLanguages() {
        val temp = _sourceLanguage.value
        _sourceLanguage.value = _targetLanguage.value
        _targetLanguage.value = temp
    }

    // Translate the text
    fun translateText(text: String) {
        // Create translate request
        val request = TranslateRequest(
            q = text,
            source = _sourceLanguage.value.code,
            target = _targetLanguage.value.code
        )

        viewModelScope.launch {
            when (val result = repository.translateText(request)) {
                is Result.Success -> {
                    // Set translated text when successful
                    _translatedText.value = result.data
                }
                is Result.Error -> {
                    // Set error message when translation fails
                    _translatedText.value = "Translation failed"
                    _error.value = "Translation failed: ${result.error.name}"
                }
            }
        }
    }
}

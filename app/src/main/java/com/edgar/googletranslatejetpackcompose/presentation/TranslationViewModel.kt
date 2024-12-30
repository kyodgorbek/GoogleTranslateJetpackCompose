package com.edgar.googletranslatejetpackcompose.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edgar.googletranslatejetpackcompose.domain.Language
import com.edgar.googletranslatejetpackcompose.core.domain.util.Result
import com.edgar.googletranslatejetpackcompose.core.presentation.selection.getSupportedLanguages
import com.edgar.googletranslatejetpackcompose.data.remote.TranslateRequest
import com.edgar.googletranslatejetpackcompose.data.remote.repository.TranslationRepository
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

    // Source input text
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    init {
        loadSupportedLanguages()
    }

    // Load supported languages from predefined list
    private fun loadSupportedLanguages() {
        _supportedLanguages.value = getSupportedLanguages() // Using the predefined function
    }

    // Set source language
    fun setSourceLanguage(language: Language) {
        _sourceLanguage.value = language
        translateText(getTranslatedText()) // Trigger translation after language change
    }

    // Set target language
    fun setTargetLanguage(language: Language) {
        _targetLanguage.value = language
        translateText(getTranslatedText()) // Trigger translation after language change
    }

    // Swap source and target languages
    fun swapLanguages() {
        val temp = _sourceLanguage.value
        _sourceLanguage.value = _targetLanguage.value
        _targetLanguage.value = temp
        _inputText.value = translatedText.value // Set the inputText to the currently translated text
        translateText(getTranslatedText()) // Trigger translation after language swap
    }

    // Get the current input text that should be translated
    private fun getTranslatedText(): String {
        return _inputText.value // This method gets the current input text for translation
    }

    // Translate the text
    fun translateText(text: String) {
        if (text.isEmpty()) {
            _translatedText.value = "" // Clear translation if input text is empty
            return
        }

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

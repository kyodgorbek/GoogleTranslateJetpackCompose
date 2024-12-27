package com.edgar.googletranslatejetpackcompose.presentation


import androidx.lifecycle.ViewModel
import com.edgar.googletranslatejetpackcompose.data.Language
import com.edgar.googletranslatejetpackcompose.data.TranslateRequest
import com.edgar.googletranslatejetpackcompose.data.repository.TranslationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TranslationViewModel(private val repository: TranslationRepository) : ViewModel() {

    // StateFlow for managing source and target language
    private val _sourceLanguage = MutableStateFlow(Language("en", "English"))
    var sourceLanguage: StateFlow<Language> = _sourceLanguage

    private val _targetLanguage = MutableStateFlow(Language("fr", "French"))
    var targetLanguage: StateFlow<Language> = _targetLanguage

    // StateFlow for the translation result
    private val _translatedText = MutableStateFlow("")
    var translatedText: StateFlow<String> = _translatedText

    // StateFlow for supported languages list
    private val _supportedLanguages = MutableStateFlow<List<Language>>(emptyList())
    var supportedLanguages: StateFlow<List<Language>> = _supportedLanguages

    // Load supported languages
    init {
        loadSupportedLanguages()
    }

    private fun loadSupportedLanguages() {
        viewModelScope.launch {
            try {
                val languages = repository.getSupportedLanguages()
                _supportedLanguages.value = languages
            } catch (e: Exception) {
                // Handle error fetching languages
            }
        }
    }

    // Swap source and target languages
    fun swapLanguages() {
        val temp = _sourceLanguage.value
        _sourceLanguage.value = _targetLanguage.value
        _targetLanguage.value = temp
    }

    // Translate text using the current source and target language
    fun translateText(text: String) {
        val request = TranslateRequest(
            q = text,
            source = _sourceLanguage.value.code,
            target = _targetLanguage.value.code
        )
        viewModelScope.launch {
            try {
                val result = repository.translateText(request)
                _translatedText.value = result
            } catch (e: Exception) {
                _translatedText.value = "Translation failed"
            }
        }
    }
}

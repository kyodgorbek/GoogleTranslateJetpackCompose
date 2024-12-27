package com.edgar.googletranslatejetpackcompose.data

import kotlinx.serialization.Serializable

@Serializable
data class SupportedLanguagesResponse(val languages: List<LanguageData>)
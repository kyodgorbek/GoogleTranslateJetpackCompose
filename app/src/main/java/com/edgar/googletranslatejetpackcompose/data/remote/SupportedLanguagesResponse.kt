package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SupportedLanguagesResponse(val languages: List<LanguageData>)
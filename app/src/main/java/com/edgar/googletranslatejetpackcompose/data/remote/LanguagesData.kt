package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LanguagesData(
    @SerialName("languages")
    val languageResponses: List<LanguageResponse>?
)
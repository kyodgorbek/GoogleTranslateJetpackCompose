package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SupportedLanguagesResponse(
    @SerialName("data")
    val data: LanguagesData?
)
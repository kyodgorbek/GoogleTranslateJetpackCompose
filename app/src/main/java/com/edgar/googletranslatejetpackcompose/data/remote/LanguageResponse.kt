package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LanguageResponse(
    @SerialName("code") val code: String? = null,
    @SerialName("name") val name: String? = null
)

package com.edgar.googletranslatejetpackcompose.data

import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequest(val q: String, val source: String, val target: String)

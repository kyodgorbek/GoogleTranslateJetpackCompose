package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TranslateRequest(val q: String, val source: String, val target: String)

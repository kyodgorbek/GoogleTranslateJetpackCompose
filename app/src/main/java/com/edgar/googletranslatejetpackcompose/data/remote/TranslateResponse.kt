package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponse(val data: TranslationsData?)
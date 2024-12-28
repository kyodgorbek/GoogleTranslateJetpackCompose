package com.edgar.googletranslatejetpackcompose.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class TranslationsData(val translations: List<Translation>)

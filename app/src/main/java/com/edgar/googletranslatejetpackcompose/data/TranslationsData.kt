package com.edgar.googletranslatejetpackcompose.data

import kotlinx.serialization.Serializable

@Serializable
data class TranslationsData(val translations: List<Translation>)

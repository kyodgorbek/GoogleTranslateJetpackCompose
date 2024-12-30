package com.edgar.googletranslatejetpackcompose.core.mappers

import com.edgar.googletranslatejetpackcompose.domain.Language
import com.edgar.googletranslatejetpackcompose.data.remote.LanguageResponse

class LanguageMapper {
    fun mapToDomain(languageResponse: LanguageResponse): Language? {
        return if (languageResponse.code != null && languageResponse.name != null) {
            Language(languageResponse.code, languageResponse.name)
        } else {
            null
        }
    }
}

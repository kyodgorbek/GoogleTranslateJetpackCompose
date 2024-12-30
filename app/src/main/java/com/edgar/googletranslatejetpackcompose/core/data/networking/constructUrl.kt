package com.edgar.googletranslatejetpackcompose.core.data.networking

import com.edgar.googletranslatejetpackcompose.BuildConfig

fun constructUrl(endpoint: String): String {
    return when {
        endpoint.contains(BuildConfig.BASE_URL) -> endpoint
        endpoint.startsWith("/") -> BuildConfig.BASE_URL + endpoint.drop(1)
        else -> BuildConfig.BASE_URL + endpoint
    }
}
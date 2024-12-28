package com.edgar.googletranslatejetpackcompose.presentation.selection

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.edgar.googletranslatejetpackcompose.data.remote.Language
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionScreen(
    isSource: Boolean,
    viewModel: TranslationViewModel,
    onBack: () -> Unit
) {
    val supportedLanguages = listOf(
        Language("en", "English"),
        Language("es", "Spanish"),
        Language("en", "English"),
        Language("fr", "French"),
        Language("de", "German"),
        Language("it", "Italian"),
                Language("ru", "Russian"),
        Language("uz", "Uzbek")

    )

    val currentLanguage = if (isSource) viewModel.sourceLanguage.collectAsState().value else viewModel.targetLanguage.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Language") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            supportedLanguages.forEach { language ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            if (isSource) {
                                viewModel.setSourceLanguage(language)
                            } else {
                                viewModel.setTargetLanguage(language)
                            }
                            onBack()
                        }
                ) {
                    Text(
                        text = language.name,
                        color = if (language == currentLanguage) Color.Blue else Color.Black
                    )
                }
            }
        }
    }
}
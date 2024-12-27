package com.edgar.googletranslatejetpackcompose.presentation.translation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TranslationScreen(navController: NavController, viewModel: TranslationViewModel) {
    var inputText by remember { mutableStateOf("") }
    var translatedText by remember { mutableStateOf("") }

    val sourceLanguage = viewModel.sourceLanguage.value
    val targetLanguage = viewModel.targetLanguage.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("Enter text") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                translatedText = viewModel.translateText(inputText).toString()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Translate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Translated Text:")
        Text(text = translatedText)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = { navController.navigate("select_source") }) {
                Text("Source: ${sourceLanguage.name}")
            }
            Button(onClick = { navController.navigate("select_target") }) {
                Text("Target: ${targetLanguage.name}")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.swapLanguages() }) {
            Text("Swap")
        }
    }
}

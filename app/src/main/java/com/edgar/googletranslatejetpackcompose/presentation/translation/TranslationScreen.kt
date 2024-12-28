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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edgar.googletranslatejetpackcompose.R
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TranslationScreen(navController: NavController, viewModel: TranslationViewModel) {
    val sourceLanguage by viewModel.sourceLanguage.collectAsState()
    val targetLanguage by viewModel.targetLanguage.collectAsState()
    val translatedText by viewModel.translatedText.collectAsState()

    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Swap icon at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { viewModel.swapLanguages() }) {
                Icon(
                    painter = painterResource(id = R.drawable.swap),
                    contentDescription = "Swap Languages",
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Language selection buttons below the swap icon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { navController.navigate("select_source") }) {
                Text("Source: ${sourceLanguage.name}")
            }
            Button(onClick = { navController.navigate("select_target") }) {
                Text("Target: ${targetLanguage.name}")
            }
        }

        // Source and target text fields side by side
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Source (${sourceLanguage.name}):")
                TextField(
                    value = inputText,
                    onValueChange = {
                        inputText = it
                        viewModel.translateText(it)
                    },
                    label = { Text("Enter text") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Target (${targetLanguage.name}):")
                TextField(
                    value = translatedText,
                    onValueChange = {},
                    label = { Text("Translated text") },
                    enabled = false, // Make read-only
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

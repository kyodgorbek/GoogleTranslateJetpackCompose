package com.edgar.googletranslatejetpackcompose.presentation.translation

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edgar.googletranslatejetpackcompose.R
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(navController: NavController, viewModel: TranslationViewModel) {
    val sourceLanguage by viewModel.sourceLanguage.collectAsState()
    val targetLanguage by viewModel.targetLanguage.collectAsState()
    val translatedText by viewModel.translatedText.collectAsState()
    val supportedLanguages by viewModel.supportedLanguages.collectAsState()

    var inputText by remember { mutableStateOf("") } // User input text
    var showLanguageList by remember { mutableStateOf(false) } // Toggles language selection view
    var isSourceLanguage by remember { mutableStateOf(true) } // Determines if source or target list
    var searchQuery by remember { mutableStateOf("") } // Search query
    var isSearchActive by remember { mutableStateOf(false) } // Toggles search query visibility

    // Filter the languages based on the search query
    val filteredLanguages = supportedLanguages.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    // Animate the width of the TextField when search is active
    val searchTextFieldWidth = animateDpAsState(
        targetValue = if (isSearchActive) 320.dp else 0.dp, // Adjust the width based on the state
        animationSpec = tween(durationMillis = 300) // Optional animation duration
    )

    // Initialize Text-to-Speech
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    // Initialize the TTS object and set the language
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val langResult =
                    tts?.setLanguage(Locale(targetLanguage.code)) // Set language for TTS
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported or missing data")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
    }

    // Function to convert text to speech
    fun speakText(text: String) {
        if (text.isNotBlank()) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    // Handle resource cleanup for TTS
    DisposableEffect(LocalContext.current) {
        onDispose {
            tts?.stop()
            tts?.shutdown()
        }
    }

    // Speech-to-text functionality using ActivityResultContracts
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val speechResults =
                    result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                speechResults?.let { inputText = it.firstOrNull() ?: "" }
                viewModel.translateText(inputText)
            }
        }
    )

    fun startSpeechRecognition() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something")
        }
        activityResultLauncher.launch(speechRecognizerIntent)
    }

    Scaffold(
        topBar = {
            if (showLanguageList) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = if (isSourceLanguage) "Translate From" else "Translate To",
                            maxLines = 1,
                            color = if (isSearchActive) MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.6f
                            ) else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { showLanguageList = false }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = !isSearchActive }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon"
                            )
                        }

                        // Conditionally show the search TextField
                        if (isSearchActive) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                leadingIcon = null, // Remove the leading search icon in the TextField
                                placeholder = { Text("Search") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp)
                                    .width(searchTextFieldWidth.value), // Use animated width
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.6f
                                    )
                                )
                            )
                        }
                    }
                )
            }
        }
    ) { innerPadding: PaddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!showLanguageList) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Swap Icon
                    IconButton(
                        onClick = { viewModel.swapLanguages() },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.swap),
                            contentDescription = "Swap Languages",
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Source and Target Buttons in the same row
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = {
                                isSourceLanguage = true
                                showLanguageList = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        ) {
                            Text("Source: ${sourceLanguage.name}")
                        }

                        Button(
                            onClick = {
                                isSourceLanguage = false
                                showLanguageList = true
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Target: ${targetLanguage.name}")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input and Translated Text Fields in the same row
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = inputText,
                            onValueChange = {
                                inputText = it
                                viewModel.translateText(it)
                            },
                            label = { Text("Enter Text") },
                            placeholder = { Text("Type text to translate") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )

                        TextField(
                            value = translatedText,
                            onValueChange = {},
                            label = { Text("Translated Text") },
                            enabled = false,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to trigger TTS for the translated text
                    Button(
                        onClick = {
                            if (translatedText.isNotEmpty()) {
                                speakText(translatedText) // Speak the translated text
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Speak Translation")
                    }

                    // Add Speech-to-Text button
                    IconButton(
                        onClick = { startSpeechRecognition() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.microphone), // Replace with your mic icon...
                            contentDescription = "Start Speech Recognition"
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredLanguages) { language ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    if (isSourceLanguage) {
                                        viewModel.setSourceLanguage(language)
                                    } else {
                                        viewModel.setTargetLanguage(language)
                                    }
                                    showLanguageList = false
                                }
                        ) {
                            Text(
                                text = language.name,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

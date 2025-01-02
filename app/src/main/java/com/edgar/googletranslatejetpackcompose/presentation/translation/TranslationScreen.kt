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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.edgar.googletranslatejetpackcompose.R
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(navController: NavController, viewModel: TranslationViewModel) {
    val sourceLanguage by viewModel.sourceLanguage.collectAsState() // Tag: Source language state
    val targetLanguage by viewModel.targetLanguage.collectAsState() // Tag: Target language state
    val translatedText by viewModel.translatedText.collectAsState() // Tag: Translated text state
    val supportedLanguages by viewModel.supportedLanguages.collectAsState() // Tag: Supported languages list

    var inputText by remember { mutableStateOf("") } // Tag: Input text state
    var showLanguageList by remember { mutableStateOf(false) } // Tag: Language list visibility state
    var isSourceLanguage by remember { mutableStateOf(true) } // Tag: Source language selection state
    var searchQuery by remember { mutableStateOf("") } // Tag: Search query for filtering languages
    var isSearchActive by remember { mutableStateOf(false) } // Tag: Search active state

    // Filter the languages based on the search query
    val filteredLanguages = supportedLanguages.filter {
        it.name.contains(searchQuery, ignoreCase = true) // Tag: Filtered languages list
    }

    // Animate the width of the TextField when search is active
    val searchTextFieldWidth = animateDpAsState(
        targetValue = if (isSearchActive) 320.dp else 0.dp, // Tag: Animated TextField width
        animationSpec = tween(durationMillis = 300) // Tag: Animation spec for width change
    )

    // Initialize Text-to-Speech
    var tts: TextToSpeech? by remember { mutableStateOf(null) } // Tag: TTS state
    val context = LocalContext.current // Tag: Local context

    // Initialize the TTS object and set the language
    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val langResult = tts?.setLanguage(Locale(targetLanguage.code)) // Tag: Set language for TTS
                if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported or missing data") // Tag: Error handling for TTS
                }
            } else {
                Log.e("TTS", "Initialization failed") // Tag: Error handling for TTS init failure
            }
        }
    }

    // Function to convert text to speech
    fun speakText(text: String) {
        if (text.isNotBlank()) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) // Tag: Convert text to speech
        }
    }

    // Handle resource cleanup for TTS
    DisposableEffect(LocalContext.current) {
        onDispose {
            tts?.stop() // Tag: Stop TTS when disposed
            tts?.shutdown() // Tag: Shutdown TTS when disposed
        }
    }

    // Speech-to-text functionality using ActivityResultContracts
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val speechResults = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                speechResults?.let { inputText = it.firstOrNull() ?: "" } // Tag: Handle speech recognition result
                viewModel.translateText(inputText) // Tag: Trigger translation after speech input
            }
        }
    )

    fun startSpeechRecognition() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) // Tag: Speech recognition intent setup
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something") // Tag: Speech prompt setup
        }
        activityResultLauncher.launch(speechRecognizerIntent) // Tag: Launch speech recognition
    }

    Scaffold(
        topBar = {
            if (showLanguageList) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = if (isSourceLanguage) "Translate From" else "Translate To", // Tag: Title text for language selection
                            maxLines = 1,
                            color = if (isSearchActive) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { showLanguageList = false }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back" // Tag: Back navigation
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { isSearchActive = !isSearchActive }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search Icon",
                                modifier = Modifier.testTag("SearchIcon") // Tag: Search icon visibility
                                 // Tag: Search icon visibility
                                    // Tag: Search icon toggle
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
                                    .width(searchTextFieldWidth.value)
                                    .testTag("SearchTextField"), // Tag: Search TextField visibility and animation
                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                            .testTag("SwapLanguagesButton") // Tag: Swap languages button
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.swap),
                            contentDescription = "Swap Languages", // Tag: Swap languages icon
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
                                .testTag("SourceLanguageButton") // Tag: Source language button
                        ) {
                            Text("Source: ${sourceLanguage.name}") // Tag: Source language button
                        }

                        Button(
                            onClick = {
                                isSourceLanguage = false
                                showLanguageList = true
                            },
                            modifier = Modifier.weight(1f)
                                .testTag("TargetLanguageButton") // Tag: Target language button
                        ) {
                            Text("Target: ${targetLanguage.name}") // Tag: Target language button
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Input and Translated Text Fields in the same row
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextField(
                            value = inputText,
                            onValueChange = {
                                inputText = it
                                viewModel.translateText(it) // Tag: Input text change triggers translation
                            },
                            label = { Text("Enter Text") },
                            placeholder = { Text("Type text to translate") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                                .testTag("InputTextField") // Tag: Input TextField
                        )

                        TextField(
                            value = translatedText,
                            onValueChange = {},
                            label = { Text("Translated Text") },
                            enabled = false,
                            modifier = Modifier.weight(1f)
                                .testTag("TranslatedTextField") // Tag: Translated TextField
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to trigger TTS for the translated text
                    Button(
                        onClick = {
                            if (translatedText.isNotEmpty()) {
                                speakText(translatedText) // Tag: Speak translated text button
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag("SpeakTranslationButton") // Tag: Speak translation button
                    ) {
                        Text("Speak Translation")
                    }

                    // Add Speech-to-Text button
                    IconButton(
                        onClick = { startSpeechRecognition() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag("TranslateButton") // Tag: Speech-to-text button
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.microphone), // Replace with your mic icon...
                            contentDescription = "Start Speech Recognition" // Tag: Speech recognition icon
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
                                        viewModel.setSourceLanguage(language) // Tag: Set source language
                                    } else {
                                        viewModel.setTargetLanguage(language) // Tag: Set target language
                                    }
                                    showLanguageList = false
                                }
                        ) {
                            Text(
                                text = language.name,
                                modifier = Modifier.padding(start = 8.dp)
                                .testTag("LanguageListItem") // Tag: Language list item
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.edgar.googletranslatejetpackcompose.presentation.translation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import com.edgar.googletranslatejetpackcompose.FakeTranslationRepository
import com.edgar.googletranslatejetpackcompose.presentation.TranslationViewModel
import io.ktor.client.HttpClient
import org.junit.Rule
import org.junit.Test

class TranslationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Composable
    fun TranslationScreenPreview(shouldReturnError: Boolean = false) {
        val fakeRepository = FakeTranslationRepository(HttpClient()) // Create the fake repository
        fakeRepository.shouldReturnError = shouldReturnError // Set the error flag if needed
        fakeRepository.translatedText =
            if (shouldReturnError) "Error" else "Translated text" // Set mock translated text
        val viewModel = TranslationViewModel(fakeRepository) // Pass the repository to the ViewModel
        TranslationScreen(navController = rememberNavController(), viewModel = viewModel)
    }




    @Test
    fun test_translateButtonAndTextInput() {
        composeTestRule.setContent {
            MaterialTheme {
                TranslationScreenPreview(shouldReturnError = false) // Simulate success
            }
        }

        // Ensure the InputTextField is displayed
        composeTestRule.onNodeWithTag("InputTextField")
            .assertIsDisplayed()

        // Enter the text to translate
        composeTestRule.onNodeWithTag("InputTextField")
            .performTextInput("Hello, world!")

        // Assert that the translated text is shown
        composeTestRule.onNodeWithTag("TranslatedTextField")
            .assertTextContains("Translated text")
    }

    @Test
    fun test_translateErrorHandling() {
        composeTestRule.setContent {
            MaterialTheme {
                TranslationScreenPreview(shouldReturnError = true) // Simulate error
            }
        }

        // Ensure the InputTextField is displayed
        composeTestRule.onNodeWithTag("InputTextField")
            .assertIsDisplayed()

        // Enter the text to translate
        composeTestRule.onNodeWithTag("InputTextField")
            .performTextInput("Hello, world!")

        // Wait for the UI to update after the error state is triggered
        composeTestRule.waitForIdle()

        // Assert that the error message is shown (match the actual error message)
        composeTestRule.onNodeWithTag("TranslatedTextField")
            .assertTextContains("Translation failed") // Change this to the correct error message
    }


    @Test
    fun test_speakButton() {
        composeTestRule.setContent {
            MaterialTheme {
                TranslationScreenPreview()
            }
        }

        // Ensure the SpeakTranslationButton is displayed and perform a click
        composeTestRule.onNodeWithTag("SpeakTranslationButton")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("SpeakTranslationButton")
            .performClick()
    }

    @Test
    fun test_speechToTextButton() {
        composeTestRule.setContent {
            MaterialTheme {
                TranslationScreenPreview()
            }
        }

        // Ensure the TranslateButton is displayed and perform a click
        composeTestRule.onNodeWithTag("TranslateButton")
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("TranslateButton")
            .performClick()
    }
}

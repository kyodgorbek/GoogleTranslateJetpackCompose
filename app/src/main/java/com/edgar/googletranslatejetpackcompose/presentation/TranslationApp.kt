package com.edgar.googletranslatejetpackcompose.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edgar.googletranslatejetpackcompose.presentation.selection.LanguageSelectionScreen

import com.edgar.googletranslatejetpackcompose.presentation.translation.TranslationScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TranslationApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: TranslationViewModel = koinViewModel()

    NavHost(navController = navController, startDestination = "translation") {
        composable("translation") {
            TranslationScreen(navController = navController, viewModel = viewModel)
        }
        composable("select_source") {
            LanguageSelectionScreen(
               isSource = true,
               viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        composable("select_target") {
            LanguageSelectionScreen(
                isSource = false,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

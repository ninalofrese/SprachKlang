package br.dev.nina.sprachklang.feature_word.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.dev.nina.sprachklang.feature_word.presentation.WordRoute

const val WORD_DETAIL_ROUTE = "word"

fun NavController.navigateToWord(wordId: Int) {
    navigate("$WORD_DETAIL_ROUTE/$wordId")
}

fun NavGraphBuilder.wordScreen(
    onNavigateBack: () -> Unit,
) {
    composable(
        "word/{entryId}",
        arguments = listOf(navArgument("entryId") { defaultValue = 32 })
    ) { backStackEntry ->
        backStackEntry.arguments?.getInt("entryId")?.let {
            WordRoute(it, onNavigateBack)
        }
    }
}

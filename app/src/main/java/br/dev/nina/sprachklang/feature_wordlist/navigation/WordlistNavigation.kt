package br.dev.nina.sprachklang.feature_wordlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.dev.nina.sprachklang.feature_wordlist.WordlistRoute

const val WORDLIST_ROUTE = "wordlist"

fun NavController.navigateToWordlist(wordlistId: Int) {
    navigate("$WORDLIST_ROUTE/$wordlistId")
}

fun NavGraphBuilder.wordlistScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWord: (Int) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    composable(
        "wordlist/{wordlistId}",
        arguments = listOf(navArgument("wordlistId") { defaultValue = 32 })
    ) { backStackEntry ->
        backStackEntry.arguments?.getInt("wordlistId")?.let {
            WordlistRoute(
                it,
                onNavigateBack = onNavigateBack,
                onNavigateToWord = onNavigateToWord,
                onNavigateToSearch = onNavigateToSearch
            )
        }
    }
}

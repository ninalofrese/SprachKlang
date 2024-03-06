package br.dev.nina.sprachklang.dictionarysearch.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.nina.sprachklang.dictionarysearch.presentation.DictionarySearchRoute

const val SEARCH_ROUTE = "search"

fun NavController.navigateToSearch() {
    navigate(SEARCH_ROUTE)
}

fun NavGraphBuilder.searchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWordDetail: (Int) -> Unit
) {
    composable(route = SEARCH_ROUTE) {
        DictionarySearchRoute(
            onNavigateToWordDetail = {
                onNavigateToWordDetail(it)
            },
            onNavigateBack = onNavigateBack
        )
    }
}

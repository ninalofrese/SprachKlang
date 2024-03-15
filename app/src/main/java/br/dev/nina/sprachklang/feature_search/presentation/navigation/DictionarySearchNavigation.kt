package br.dev.nina.sprachklang.feature_search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.nina.sprachklang.feature_search.presentation.SearchRoute

const val SEARCH_ROUTE = "search"

fun NavController.navigateToSearch() {
    navigate(SEARCH_ROUTE)
}

fun NavGraphBuilder.searchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWordDetail: (Int) -> Unit
) {
    composable(route = SEARCH_ROUTE) {
        SearchRoute(
            onNavigateToWordDetail = {
                onNavigateToWordDetail(it)
            },
            onNavigateBack = onNavigateBack
        )
    }
}

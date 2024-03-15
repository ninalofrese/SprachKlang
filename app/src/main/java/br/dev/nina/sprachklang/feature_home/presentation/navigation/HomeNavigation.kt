package br.dev.nina.sprachklang.feature_home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.nina.sprachklang.feature_home.presentation.HomeRoute

const val HOME_ROUTE = "home"

fun NavController.navigateToHome() {
    navigate(HOME_ROUTE)
}

fun NavGraphBuilder.homeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToWordList: (Int) -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeRoute(
            onNavigateToSearch = onNavigateToSearch,
            onNavigateToWordlist = onNavigateToWordList
        )
    }
}

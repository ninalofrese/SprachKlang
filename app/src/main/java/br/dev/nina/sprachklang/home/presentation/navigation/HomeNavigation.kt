package br.dev.nina.sprachklang.home.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.dev.nina.sprachklang.home.presentation.HomeScreen

const val HOME_ROUTE = "home"

fun NavController.navigateToHome() {
    navigate(HOME_ROUTE)
}

fun NavGraphBuilder.homeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToWordDetail: (Int) -> Unit
) {
    composable(route = HOME_ROUTE) {
        HomeScreen(onNavigateToWordDetail = { onNavigateToWordDetail(it) })
    }
}

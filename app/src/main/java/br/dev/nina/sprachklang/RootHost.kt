package br.dev.nina.sprachklang

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.dev.nina.sprachklang.dictionarysearch.presentation.navigation.navigateToSearch
import br.dev.nina.sprachklang.dictionarysearch.presentation.navigation.searchScreen
import br.dev.nina.sprachklang.home.presentation.navigation.HOME_ROUTE
import br.dev.nina.sprachklang.home.presentation.navigation.homeScreen
import br.dev.nina.sprachklang.word.presentation.navigation.navigateToWord
import br.dev.nina.sprachklang.word.presentation.navigation.wordScreen
import br.dev.nina.sprachklang.wordlist.navigation.navigateToWordlist
import br.dev.nina.sprachklang.wordlist.navigation.wordlistScreen

@Composable
fun RootHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE
    ) {
        homeScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToSearch = { navController.navigateToSearch() },
            onNavigateToWordList = { navController.navigateToWordlist(it) }
        )
        searchScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToWordDetail = { navController.navigateToWord(it) }
        )
        wordScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
        )
        wordlistScreen(
            onNavigateBack = {
                navController.popBackStack()
            },
            onNavigateToWord = { navController.navigateToWord(it) },
            onNavigateToSearch = { navController.navigateToSearch() }
        )
    }
}

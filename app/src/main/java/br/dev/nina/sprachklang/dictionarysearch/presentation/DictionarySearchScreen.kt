package br.dev.nina.sprachklang.dictionarysearch.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.nina.sprachklang.home.presentation.HomeViewModel

@Composable
fun DictionarySearchRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToWordDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val searchStateHolder = viewModel.searchStateHolder

    DictionarySearchScreen(
        state = searchStateHolder.state,
        onSearchQueryChange = {
            searchStateHolder.onEvent(DictionarySearchEvent.OnSearchQueryChange(it))
        },
        onLoadNextItems = {
            searchStateHolder.onEvent(DictionarySearchEvent.OnLoadNextItems)
        },
        onNavigateToWordDetail = {
            onNavigateToWordDetail(it)
        },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun DictionarySearchScreen(
    state: DictionarySearchUiState,
    onSearchQueryChange: (String) -> Unit,
    onLoadNextItems: () -> Unit,
    onNavigateToWordDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            state = state,
            onSearchQueryChange = {
                onSearchQueryChange(it)
            },
            onNavigateBack = onNavigateBack
        )

        if (state.items.isNotEmpty()) {
            DictionarySearchResults(
                state = state,
                loadNextItems = onLoadNextItems,
                modifier = Modifier
                    .fillMaxSize(),
                onClick = { onNavigateToWordDetail(it) }
            )
        }
    }
}

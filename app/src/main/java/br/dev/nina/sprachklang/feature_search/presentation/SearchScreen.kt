package br.dev.nina.sprachklang.feature_search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.nina.sprachklang.core.presentation.preview.DevicePreview
import br.dev.nina.sprachklang.feature_search.presentation.components.DictionarySearchResults
import br.dev.nina.sprachklang.feature_search.presentation.components.SearchBar
import br.dev.nina.sprachklang.feature_search.presentation.preview.DictionarySearchUiStateProvider
import br.dev.nina.sprachklang.ui.theme.SprachKlangTheme

@Composable
fun SearchRoute(
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToWordDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val state = viewModel.state

    SearchScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateToWordDetail = {
            onNavigateToWordDetail(it)
        },
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (DictionarySearchEvent) -> Unit,
    onNavigateToWordDetail: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            SearchBar(
                query = state.query,
                onSearchQueryChange = {
                    onEvent(DictionarySearchEvent.OnSearchQueryChange(it))
                },
                onNavigateBack = onNavigateBack,
                onClearQuery = {
                    onEvent(DictionarySearchEvent.OnSearchQueryChange(""))
                }
            )

            if (state.items.isNotEmpty()) {
                DictionarySearchResults(
                    state = state,
                    loadNextItems = { onEvent(DictionarySearchEvent.OnLoadNextItems)},
                    modifier = Modifier
                        .fillMaxSize(),
                    onClick = { onNavigateToWordDetail(it) }
                )
            }
        }
    }
}

@DevicePreview
@Composable
private fun DictionarySearchPreview(
    @PreviewParameter(DictionarySearchUiStateProvider::class)
    state: SearchState
) {
    SprachKlangTheme {
        SearchScreen(state = state, {}, {}, {})
    }
}

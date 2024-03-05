package br.dev.nina.sprachklang.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.nina.sprachklang.dictionarysearch.presentation.DictionarySearchEvent
import br.dev.nina.sprachklang.dictionarysearch.presentation.DictionarySearchResults
import br.dev.nina.sprachklang.dictionarysearch.presentation.SearchBar

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToWordDetail: (Int) -> Unit
) {
    val searchStateHolder = viewModel.searchStateHolder

    Column(modifier = Modifier.semantics { isTraversalGroup = true }) {
        SearchBar(
            state = searchStateHolder.state,
            onSearchQueryChange = {
                viewModel.searchStateHolder.onEvent(DictionarySearchEvent.OnSearchQueryChange(it))
            }
        )
        if (searchStateHolder.state.items.isNotEmpty()) {
            DictionarySearchResults(
                state = searchStateHolder.state,
                loadNextItems = {
                    viewModel.searchStateHolder.onEvent(DictionarySearchEvent.OnLoadNextItems)
                },
                modifier = Modifier
                    .fillMaxSize(),
                onClick = { onNavigateToWordDetail(it) }
            )
        } else {
            Text(text = "Home")
        }
    }
}

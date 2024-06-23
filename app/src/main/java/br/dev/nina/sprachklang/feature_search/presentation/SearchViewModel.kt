
package br.dev.nina.sprachklang.feature_search.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.util.DispatcherProvider
import br.dev.nina.sprachklang.core.util.pagination.QueryPaginator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DictionaryRepository,
    private val dispatcher: DispatcherProvider
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private var searchJob: Job? = null

    private val paginator = QueryPaginator(
        initialKey = state.page,
        onLoadUpdated = { isLoading ->
            state = state.copy(isLoading = isLoading)
        },
        onRequest = { nextPage, query ->
            withContext(dispatcher.io) {
                repository.searchWord(query, nextPage, 20)
            }
        },
        getNextKey = {
            state.page + 1
        },
        onError = { error ->
            state = state.copy(error = error)
        },
        onSuccess = { newItems, newKey ->
            withContext(dispatcher.mainImmediate) {
                state = state.copy(
                    items = if (state.page == 0) newItems else state.items + newItems,
                    page = newKey,
                    endReached = newItems.isEmpty()
                )
            }
        }
    )

    init {
        loadNextItems()
    }

    fun onEvent(event: DictionarySearchEvent) {
        when(event) {
            DictionarySearchEvent.OnLoadNextItems -> {
                loadNextItems()
            }
            is DictionarySearchEvent.OnSearchQueryChange -> {
                state = state.copy(
                    query = event.query, page = 0
                )
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    paginator.reset()
                    loadNextItems()
                }
            }
        }
    }

    private fun loadNextItems(query: String = state.query.lowercase()) {
        viewModelScope.launch {
            paginator.loadNextItems(query)
        }
    }
}

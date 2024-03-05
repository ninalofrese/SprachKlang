package br.dev.nina.sprachklang.dictionarysearch.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.util.pagination.QueryPaginator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DictionarySearchState @Inject constructor(
    private val repository: DictionaryRepository,
    private val coroutineScope: CoroutineScope,
) {

    var state by mutableStateOf(DictionarySearchUiState())
        private set

    private var searchJob: Job? = null

    private val paginator = QueryPaginator(
        initialKey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading = it)
        },
        onRequest = { nextPage, query ->
            withContext(Dispatchers.IO) {
                repository.searchWord(query, nextPage, 20)
            }
        },
        getNextKey = {
            state.page + 1
        },
        onError = {
            state = state.copy(error = it)
        },
        onSuccess = { newItems, newKey ->
            withContext(Dispatchers.Main) {
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
                state = state.copy(query = event.query, page = 0)
                searchJob?.cancel()
                searchJob = coroutineScope.launch {
                    paginator.reset()
                    loadNextItems()
                }
            }
        }
    }

    fun loadNextItems(query: String = state.query.lowercase()) {
        coroutineScope.launch {
            paginator.loadNextItems(query)
        }
    }
}

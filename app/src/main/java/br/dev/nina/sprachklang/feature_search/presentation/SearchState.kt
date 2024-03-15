package br.dev.nina.sprachklang.feature_search.presentation

import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val items: List<Headword> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)

sealed class DictionarySearchEvent {
    data class OnSearchQueryChange(val query: String): DictionarySearchEvent()
    object OnLoadNextItems: DictionarySearchEvent()
}

package br.dev.nina.sprachklang.feature_home.presentation

import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState

data class HomeUiState(
    val wordlistState: HomeWordlistState = HomeWordlistState(),
    val newListState: NewListState = NewListState()
)

data class HomeWordlistState(
    val wordlists: List<Wordlist> = emptyList(),
    val isLoading: Boolean = true,
)

sealed interface HomeWordlistEvent {
    data class DeleteWordList(val wordlist: Wordlist): HomeWordlistEvent
    data class ShowSnackbar(val message: String): HomeWordlistEvent
}

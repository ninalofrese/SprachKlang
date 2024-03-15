package br.dev.nina.sprachklang.feature_wordlist

import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist

data class WordlistState(
    val wordlist: Wordlist? = null,
    val entries: List<Entry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface WordlistEvent {
    data class UnsaveWord(val entryId: Int): WordlistEvent
    data class ShowSnackBar(val message: String): WordlistEvent
}

package br.dev.nina.sprachklang.feature_word.presentation

import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordState

data class WordState(
    val wordDetail: WordDetailState = WordDetailState(),
    val newList: NewListState = NewListState(),
    val saveWord: SaveWordState = SaveWordState()
)
data class WordDetailState(
    val entry: Entry? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

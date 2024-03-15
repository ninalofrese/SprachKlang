package br.dev.nina.sprachklang.feature_word.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordState
import br.dev.nina.sprachklang.core.presentation.preview.PreviewParameterData
import br.dev.nina.sprachklang.feature_word.presentation.WordDetailState
import br.dev.nina.sprachklang.feature_word.presentation.WordState

class WordStateParameterProvider: PreviewParameterProvider<WordState> {
    override val values: Sequence<WordState> = sequenceOf(
        WordState(
            wordDetail = WordDetailState(
                entry = PreviewParameterData.entry
            ),
            newList = NewListState(),
            saveWord = SaveWordState(
                selectedWordlist = PreviewParameterData.wordlists.subList(2,4),
                wordlists = PreviewParameterData.wordlists
            )
        )
    )
}

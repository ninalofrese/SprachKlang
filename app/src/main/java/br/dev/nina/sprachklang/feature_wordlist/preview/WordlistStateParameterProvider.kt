package br.dev.nina.sprachklang.feature_wordlist.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.nina.sprachklang.core.presentation.preview.PreviewParameterData
import br.dev.nina.sprachklang.feature_wordlist.WordlistState

class WordlistStateParameterProvider: PreviewParameterProvider<WordlistState> {
    override val values: Sequence<WordlistState> = sequenceOf(
        WordlistState(
            PreviewParameterData.wordlist,
            listOf(PreviewParameterData.entry)
        )
    )
}

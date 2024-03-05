package br.dev.nina.sprachklang.word.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.presentation.preview.PreviewParameterData

class EntryPreviewParameterProvider : PreviewParameterProvider<Entry> {
    override val values: Sequence<Entry> = sequenceOf(
        PreviewParameterData.entry
    )
}

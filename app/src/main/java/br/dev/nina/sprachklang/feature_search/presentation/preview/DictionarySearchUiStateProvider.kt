package br.dev.nina.sprachklang.feature_search.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.nina.sprachklang.core.presentation.preview.PreviewParameterData
import br.dev.nina.sprachklang.feature_search.presentation.SearchState

class DictionarySearchUiStateProvider: PreviewParameterProvider<SearchState> {
    override val values: Sequence<SearchState> = sequenceOf(
        SearchState(
            query = "genau",
            isLoading = false,
            items = PreviewParameterData.headwords
        )
    )
}

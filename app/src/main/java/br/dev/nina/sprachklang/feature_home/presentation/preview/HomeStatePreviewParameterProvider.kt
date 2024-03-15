package br.dev.nina.sprachklang.feature_home.presentation.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import br.dev.nina.sprachklang.core.presentation.preview.PreviewParameterData
import br.dev.nina.sprachklang.feature_home.presentation.HomeUiState
import br.dev.nina.sprachklang.feature_home.presentation.HomeWordlistState

class HomeStatePreviewParameterProvider: PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState(
            wordlistState = HomeWordlistState(
                wordlists = listOf(PreviewParameterData.wordlist),
                isLoading = false
            ),
            newListState = NewListState(
                false,
                ""
            )
        )
    )
}

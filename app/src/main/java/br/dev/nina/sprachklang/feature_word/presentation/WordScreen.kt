package br.dev.nina.sprachklang.feature_word.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.presentation.components.ErrorBox
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListButton
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialog
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordButton
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordModal
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordModalEvent
import br.dev.nina.sprachklang.core.presentation.preview.DevicePreview
import br.dev.nina.sprachklang.ui.theme.SprachKlangTheme
import br.dev.nina.sprachklang.feature_word.presentation.audioplayer.AudioPlayerManager
import br.dev.nina.sprachklang.feature_word.presentation.components.WordDetail
import br.dev.nina.sprachklang.feature_word.presentation.preview.WordStateParameterProvider

@Composable
fun WordRoute(
    wordId: Int,
    onNavigationBack: () -> Unit,
    viewModel: WordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val audioPlayerState = viewModel.audioPlayerManager

    BackHandler {
        onNavigationBack()
    }

    Column {
        if (state.wordDetail.error == null) {
            state.wordDetail.entry?.let {
                WordScreen(
                    state,
                    audioPlayerState,
                    viewModel::onDialogEvent,
                    viewModel::onModalEvent,
                    onNavigationBack
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(
    state: WordState,
    audioPlayer: AudioPlayerManager,
    onDialogEvent: (NewListDialogEvent) -> Unit,
    onModalEvent: (SaveWordModalEvent) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    if (state.wordDetail.entry == null || state.wordDetail.error != null) {
        ErrorBox(message = stringResource(R.string.error_word_details), onNavigateBack)
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.go_back),
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                onNavigateBack()
                            }
                    )
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            WordDetail(
                state = state.wordDetail,
                ctaContent = {
                    SaveWordButton(
                        state = state.saveWord,
                        onEvent = onModalEvent
                    )
                },
                audioPlayer = audioPlayer
            )
            if (state.saveWord.isSavingWord) {
                SaveWordModal(
                    state = state.saveWord,
                    onEvent = onModalEvent,
                    topItem = {
                        NewListButton(onDialogEvent)
                        HorizontalDivider(Modifier.padding(vertical = 16.dp))
                    }
                )
                if (state.newList.isAddingNewList) {
                    NewListDialog(
                        listnameInput = state.newList.listnameInput,
                        onEvent = onDialogEvent
                    )
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun WordScreenPreview(
    @PreviewParameter(WordStateParameterProvider::class)
    state: WordState
) {
    SprachKlangTheme {
        WordScreen(state, AudioPlayerManager(), {}, {}, {})
    }
}

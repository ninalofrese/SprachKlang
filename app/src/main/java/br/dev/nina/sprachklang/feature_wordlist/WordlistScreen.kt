package br.dev.nina.sprachklang.feature_wordlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.presentation.components.EmptyState
import br.dev.nina.sprachklang.core.presentation.components.ErrorBox
import br.dev.nina.sprachklang.core.presentation.preview.DevicePreview
import br.dev.nina.sprachklang.core.presentation.utils.localize
import br.dev.nina.sprachklang.feature_wordlist.components.WordsFeed
import br.dev.nina.sprachklang.feature_wordlist.preview.WordlistStateParameterProvider
import br.dev.nina.sprachklang.ui.theme.SprachKlangTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Composable
fun WordlistRoute(
    wordlistId: Int,
    viewModel: WordlistViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToWord: (Int) -> Unit,
    onNavigateToSearch: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WordlistScreen(
        state = state,
        snackBarFlow = viewModel.snackBarFlow,
        onNavigateBack = onNavigateBack,
        onNavigateToWord = onNavigateToWord,
        onEvent = viewModel::onEvent,
        onNavigateToSearch = onNavigateToSearch
    )
}


@Composable
fun WordlistScreen(
    state: WordlistState,
    snackBarFlow: SharedFlow<String>,
    onNavigateBack: () -> Unit,
    onNavigateToWord: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    onEvent: (WordlistEvent) -> Unit
) {

    if (state.wordlist == null || state.error != null) {
        ErrorBox(message = stringResource(R.string.there_was_an_error_displaying_the_words_in_this_list), onNavigateBack)
    } else {
        WordlistContent(
            state = state,
            snackBarFlow = snackBarFlow,
            onNavigateBack = onNavigateBack,
            onNavigateToWord = onNavigateToWord,
            onNavigateToSearch = onNavigateToSearch,
            onEvent = onEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordlistContent(
    state: WordlistState,
    snackBarFlow: SharedFlow<String>,
    onNavigateBack: () -> Unit,
    onNavigateToWord: (Int) -> Unit,
    onNavigateToSearch: () -> Unit,
    onEvent: (WordlistEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarFlow) {
        snackBarFlow.collect { message ->
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Long)
        }
    }

    state.wordlist?.let {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = it.name,
                            modifier = Modifier.semantics {
                                heading()
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    },
                )
            }
        ) { innerPadding ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (state.entries.isNotEmpty()) {
                    WordsFeed(
                        state = state,
                        onNavigateToWord = onNavigateToWord,
                        onEvent = onEvent,
                    )
                } else {
                    EmptyState(
                        title = stringResource(R.string.no_words_found),
                        message = stringResource(R.string.empty_state_no_words, it.name)
                    ) {
                        Button(onClick = onNavigateToSearch) {
                            Text(text = stringResource(R.string.search_cta))
                        }
                    }
                }
            }
        }
    }
}

@DevicePreview
@Composable
private fun WordCollectionScreenPreview(
    @PreviewParameter(WordlistStateParameterProvider::class)
    state: WordlistState
) {
    SprachKlangTheme {
        WordlistScreen(state = state, snackBarFlow = MutableSharedFlow<String>().asSharedFlow(), {}, {}, {}, {})
    }
}



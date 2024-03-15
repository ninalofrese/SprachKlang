package br.dev.nina.sprachklang.feature_home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialog
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import br.dev.nina.sprachklang.core.presentation.preview.DevicePreview
import br.dev.nina.sprachklang.feature_home.presentation.components.SearchButton
import br.dev.nina.sprachklang.feature_home.presentation.components.WordlistsFeed
import br.dev.nina.sprachklang.feature_home.presentation.preview.HomeStatePreviewParameterProvider
import br.dev.nina.sprachklang.ui.theme.SprachKlangTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@Composable
fun HomeRoute(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit,
    onNavigateToWordlist: (Int) -> Unit
) {
    val feedState by viewModel.wordlistState.collectAsStateWithLifecycle()
    val newListState by viewModel.newListState.collectAsStateWithLifecycle()

    HomeScreen(
        feedState = feedState,
        newListState = newListState,
        snackBarFlow = viewModel.snackBarFlow,
        onNavigateToSearch = onNavigateToSearch,
        onNavigateToWordlist = onNavigateToWordlist,
        onEvent = viewModel::onEvent,
        onDialogEvent = viewModel::onDialogEvent
    )
}

@Composable
fun HomeScreen(
    newListState: NewListState,
    feedState: HomeWordlistState,
    snackBarFlow: SharedFlow<String>,
    onNavigateToSearch: () -> Unit,
    onNavigateToWordlist: (Int) -> Unit,
    onEvent: (HomeWordlistEvent) -> Unit,
    onDialogEvent: (NewListDialogEvent) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackBarFlow) {
        snackBarFlow.collect { message ->
            snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Long)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            SearchButton(onNavigateToSearch, modifier = Modifier.padding(16.dp))
            WordlistsFeed(
                feedState = feedState,
                onItemClick = {
                    onNavigateToWordlist(it)
                },
                onEvent = onEvent,
                onDialogEvent = onDialogEvent
            )
            if (newListState.isAddingNewList) {
                NewListDialog(listnameInput = newListState.listnameInput, onEvent = onDialogEvent)
            }
        }
    }
}

@DevicePreview
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeStatePreviewParameterProvider::class)
    state: HomeUiState
) {
    SprachKlangTheme {
        HomeScreen(
            onNavigateToSearch = { },
            onNavigateToWordlist = {},
            onEvent = {},
            onDialogEvent = {},
            snackBarFlow = MutableSharedFlow<String>().asSharedFlow(),
            newListState = state.newListState,
            feedState = state.wordlistState
        )
    }
}

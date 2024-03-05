package br.dev.nina.sprachklang.word.presentation

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun WordRoute(
    wordId: Int,
    onNavigationBack: () -> Unit,
    viewModel: WordViewModel = hiltViewModel()
) {
    val state = viewModel.state

    val audioPlayerState = viewModel.audioPlayerManager

    BackHandler {
        onNavigationBack()
    }

    Column {
        if (state.error == null) {
            state.entry?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WordScreen(entry = it, audioPlayerState, onNavigationBack)
                }
            }
        }
    }
}

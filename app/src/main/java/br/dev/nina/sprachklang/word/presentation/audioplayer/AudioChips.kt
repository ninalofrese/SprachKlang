package br.dev.nina.sprachklang.word.presentation.audioplayer

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.presentation.composables.CircledText
import br.dev.nina.sprachklang.core.presentation.utils.localize

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AudioChips(
    audioState: AudioUiState,
    currentPlayingMedia: Uri?,
    audioPlayer: AudioPlayerManager
) {
    val context = LocalContext.current

    val localizedString = audioState.word.localize(audioState.langCode)

    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        audioState.audioUrls.forEachIndexed { index, url ->
            val uri = Uri.parse(url)

            val isThisMediaPlaying = currentPlayingMedia == uri

            AudioChip(
                label = { Text(text = localizedString) },
                onClick = {
                    if (isThisMediaPlaying) {
                        audioPlayer.stopMedia()
                    } else {
                        audioPlayer.playMedia(context, uri)
                    }
                },
                isPlaying = isThisMediaPlaying,
                count = if (audioState.audioUrls.size > 1) index + 1 else 0
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                audioPlayer.clear()
            }
        }
    }
}

@Composable
private fun AudioChip(
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
    onClick: () -> Unit = {},
    isPlaying: Boolean,
    count: Int = 0
) {
    val color = MaterialTheme.colorScheme.secondary

    AssistChip(
        onClick = {
            onClick()
        },
        label = label,
        leadingIcon = {
            if (count > 0) {
                CircledText(text = count.toString(), circleColor = color)
            }
        },
        trailingIcon = {
            if (isPlaying) {
                Icon(painterResource(R.drawable.ic_volume_mute), contentDescription = "Stop", tint = color)
            } else {
                Icon(painterResource(R.drawable.ic_volume_up), contentDescription = "Play", tint = color)
            }
        },
        modifier = modifier
    )
}

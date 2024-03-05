package br.dev.nina.sprachklang.word.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.presentation.utils.getFullPosName
import br.dev.nina.sprachklang.core.presentation.utils.localize
import br.dev.nina.sprachklang.ui.theme.SprachKlangTheme
import br.dev.nina.sprachklang.word.presentation.audioplayer.AudioChips
import br.dev.nina.sprachklang.word.presentation.audioplayer.AudioPlayerManager
import br.dev.nina.sprachklang.word.presentation.audioplayer.AudioUiState
import br.dev.nina.sprachklang.word.presentation.preview.EntryPreviewParameterProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordScreen(
    entry: Entry,
    audioPlayer: AudioPlayerManager,
    onNavigateBack: () -> Unit = {}
) {
    val localizedWord = entry.word.localize(entry.langCode)

    val audioUiState = AudioUiState(
        audioUrls = entry.audioUrls ?: emptyList(),
        word = entry.word,
        langCode = entry.langCode
    )

    val currentPlayingMedia by audioPlayer.currentPlayingMedia.collectAsState()

    val color = MaterialTheme.colorScheme.primary

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.go_back),
                        modifier = Modifier.padding(8.dp).clickable {
                            onNavigateBack()
                        }
                    )
                },
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Surface(shadowElevation = 8.dp, color = color, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
                    Text(
                        text = localizedWord,
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.displayMedium.copy(hyphens = Hyphens.Auto)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Text(
                            text = entry.pos,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .semantics { contentDescription = getFullPosName(entry.pos) }
                        )
                        VerticalDivider(thickness = 1.dp, modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(16.dp))
                        Text(
                            text = entry.ipas.joinToString(", "),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row {
                    Column {
                        Text(text = stringResource(R.string.pronunciation), style = MaterialTheme.typography.titleMedium)
                        AudioChips(
                            audioState = audioUiState,
                            currentPlayingMedia = currentPlayingMedia,
                            audioPlayer = audioPlayer
                        )
                        HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                        Text(text = stringResource(R.string.definition), style = MaterialTheme.typography.titleMedium)

                        entry.definitions.forEach {
                            DefinitionItem(definition = it, modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DefinitionItem(
    definition: Definition,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        definition.glosses?.forEachIndexed { index, gloss ->
            Row {
                Text(text = "${index + 1}. $gloss")
            }
        }
        definition.related?.let { relatedWords ->
            Row {
                Text(text = stringResource(R.string.related_words), fontWeight = FontWeight.Bold)
                Text(text = relatedWords.joinToString(", "))
            }
        }
        definition.synonyms?.let { relatedWords ->
            Row {
                Text(text = stringResource(R.string.synonyms), fontWeight = FontWeight.Bold)
                relatedWords.forEach {
                    Text(text = it)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun WordDetailPreview(
    @PreviewParameter(EntryPreviewParameterProvider::class)
    entry: Entry
) {
    SprachKlangTheme {
        WordScreen(entry, AudioPlayerManager())
    }
}

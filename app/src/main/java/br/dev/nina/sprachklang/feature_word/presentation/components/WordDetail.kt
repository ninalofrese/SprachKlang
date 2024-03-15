package br.dev.nina.sprachklang.feature_word.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.presentation.utils.getFullPosName
import br.dev.nina.sprachklang.core.presentation.utils.localize
import br.dev.nina.sprachklang.feature_word.presentation.WordDetailState
import br.dev.nina.sprachklang.feature_word.presentation.audioplayer.AudioChips
import br.dev.nina.sprachklang.feature_word.presentation.audioplayer.AudioPlayerManager
import br.dev.nina.sprachklang.feature_word.presentation.audioplayer.AudioState

@Composable
fun WordDetail(
    state: WordDetailState,
    ctaContent: @Composable () -> Unit,
    audioPlayer: AudioPlayerManager
) {
    val entry = state.entry ?: return
    val localizedWord by remember(entry) {
        derivedStateOf {
            entry.word.localize(entry.langCode)
        }
    }

    val audioState = AudioState(
        audioUrls = entry.audioUrls ?: emptyList(),
        word = entry.word,
        langCode = entry.langCode
    )

    val currentPlayingMedia by audioPlayer.currentPlayingMedia.collectAsState()

    Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(vertical = 24.dp, horizontal = 16.dp)) {
            Text(
                text = localizedWord,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.displayMedium.copy(hyphens = Hyphens.Auto),
                modifier = Modifier.semantics { heading() }
            )
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = entry.pos,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .semantics { contentDescription = getFullPosName(entry.pos) }
                )
                if (entry.ipas.isNotEmpty()) {
                    VerticalDivider(thickness = 1.dp, modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(16.dp))
                }
                val ipas = entry.ipas.joinToString(", ")
                Text(
                    text = ipas,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.semantics { contentDescription = "IPA: $ipas" }
                )
                Spacer(modifier = Modifier.weight(1f))
                ctaContent()
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
                if (audioState.audioUrls.isNotEmpty()) {
                    Text(text = stringResource(R.string.pronunciation), style = MaterialTheme.typography.titleMedium, modifier = Modifier.semantics { heading() })
                    AudioChips(
                        audioState = audioState,
                        currentPlayingMedia = currentPlayingMedia,
                        audioPlayer = audioPlayer
                    )
                    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                }
                Text(text = stringResource(R.string.definition), style = MaterialTheme.typography.titleMedium, modifier = Modifier.semantics { heading() })

                entry.definitions.forEachIndexed { index, definition ->
                    DefinitionItem(definition = definition, modifier = Modifier
                        .padding(vertical = 8.dp)
                        .semantics(mergeDescendants = true) { }, index = index)
                }
            }
        }
    }
}

@Composable
fun DefinitionItem(
    index: Int,
    definition: Definition,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(text = "${index + 1}.", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
        Column(Modifier.weight(1f)) {
            Text(text = "${definition.glosses?.joinToString(" ")}")

            definition.related?.let { synonyms ->
                Text(text = stringResource(R.string.related_words), fontWeight = FontWeight.Bold)
                Text(text = synonyms.joinToString(", "))
            }
            definition.synonyms?.let { relatedWords ->
                Text(text = stringResource(R.string.synonyms), fontWeight = FontWeight.Bold)
                relatedWords.forEach {
                    Text(text = it)
                }
            }
        }
    }
}

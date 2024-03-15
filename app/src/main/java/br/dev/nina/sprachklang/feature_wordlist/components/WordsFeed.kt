package br.dev.nina.sprachklang.feature_wordlist.components

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.presentation.utils.localize
import br.dev.nina.sprachklang.feature_wordlist.WordlistEvent
import br.dev.nina.sprachklang.feature_wordlist.WordlistState

@Composable
fun WordsFeed(
    state: WordlistState,
    onNavigateToWord: (Int) -> Unit,
    onEvent: (WordlistEvent) -> Unit
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(state.entries) { entry ->
            WordItem(
                entry = entry,
                onItemClick = onNavigateToWord,
                onItemDelete = { onEvent(WordlistEvent.UnsaveWord(entry.id)) }
            )
        }
    }
}

@Composable
fun WordItem(
    entry: Entry,
    onItemClick: (Int) -> Unit,
    onItemDelete: () -> Unit
) {
    var openDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { onItemClick(entry.id) }) {
        Row(Modifier.padding(16.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.word.localize(entry.langCode),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(text = entry.pos, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Text(text = entry.definitions.map { it.glosses?.joinToString() }.joinToString("; "))
            }

            IconButton(
                onClick = { openDeleteDialog = true },
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete_name, entry.word))
            }
        }
    }

    if (openDeleteDialog) {
        AlertDialog(
            onDismissRequest = { openDeleteDialog = false },
            title = {
                Text(text = stringResource(R.string.delete_name, entry.word))
            },
            text = {
                Text(text = stringResource(R.string.this_will_delete_only_on_this_list, entry.word))
            },
            confirmButton = {
                TextButton(onClick = { onItemDelete() }) {
                    Text(text = stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { openDeleteDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}
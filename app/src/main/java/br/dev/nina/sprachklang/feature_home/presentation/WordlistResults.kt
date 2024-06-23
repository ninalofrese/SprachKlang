package br.dev.nina.sprachklang.feature_home.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist

@Composable
fun WordlistsResult(
    state: HomeWordlistState,
    onItemClick: (Int) -> Unit,
    onEvent: (HomeWordlistEvent) -> Unit
) {
    LazyColumn(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(state.wordlists) { wordlist ->
            WordlistItem(
                wordlist = wordlist,
                onItemClick = onItemClick,
                onItemDelete = { onEvent(HomeWordlistEvent.DeleteWordList(wordlist)) }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WordlistItem(
    wordlist: Wordlist,
    onItemClick: (Int) -> Unit,
    onItemDelete: () -> Unit
) {
    val context = LocalContext.current
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    ListItem(
        modifier = Modifier
            .padding(8.dp)
            .semantics { customActions = listOf(
                CustomAccessibilityAction(label = context.getString(R.string.delete)) {
                    showDeleteDialog = true; true
                }
            ) }
            .clickable { onItemClick(wordlist.id) },
        headlineContent = { Text(wordlist.name, style = MaterialTheme.typography.titleMedium) },
        trailingContent = {
            IconButton(onClick = { showDeleteDialog = true }, modifier = Modifier.semantics { invisibleToUser() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete_name, wordlist.name))
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            headlineColor = MaterialTheme.colorScheme.onSurface,
            trailingIconColor = MaterialTheme.colorScheme.onSurface,
        )
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = stringResource(R.string.delete_name, wordlist.name))
            },
            text = {
                Text(text = stringResource(R.string.this_will_remove_all_words_from_this_list))
            },
            confirmButton = {
                TextButton(onClick = {
                    onItemDelete()
                    showDeleteDialog = false
                }) {
                    Text(text = stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

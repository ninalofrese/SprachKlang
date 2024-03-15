package br.dev.nina.sprachklang.core.presentation.components.saveword

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveWordModal(
    state: SaveWordState,
    onEvent: (SaveWordModalEvent) -> Unit,
    topItem: @Composable LazyItemScope.() -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )

    ModalBottomSheet(
        onDismissRequest = { onEvent(SaveWordModalEvent.Show(false)) },
        sheetState = bottomSheetState,
        modifier = Modifier.semantics { paneTitle = "Select a list to save the word" }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.save_to_list),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f).semantics { heading() }
            )
            TextButton(
                onClick = { onEvent(SaveWordModalEvent.Show(false)) }
            ) {
                Text(text = stringResource(R.string.done))
            }
        }
        LazyColumn(modifier = Modifier
            .selectableGroup()
            .padding(16.dp)) {
            item {
                topItem()
            }
            if (state.wordlists.isNotEmpty()) {
                items(state.wordlists) { wordlist ->
                    val matchId = state.selectedWordlist.any { it.id == wordlist.id }
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .toggleable(
                            value = matchId,
                            onValueChange = { onEvent(SaveWordModalEvent.ToggleSelectedWordlist(wordlist)) },
                            role = Role.Checkbox,
                        ).clearAndSetSemantics {  },
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = wordlist.name, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        FilledIconToggleButton(
                            checked = matchId,
                            onCheckedChange = { onEvent(SaveWordModalEvent.ToggleSelectedWordlist(wordlist)) },
                        ) {
                            if (matchId) {
                                Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                            } else {
                                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }
    }
}

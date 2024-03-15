package br.dev.nina.sprachklang.core.presentation.components.newlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import br.dev.nina.sprachklang.R

@Composable
fun NewListDialog(
    listnameInput: String,
    onEvent: (NewListDialogEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var isError by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        modifier = modifier.semantics { paneTitle = "Add a wordlist" },
        onDismissRequest = { onEvent(NewListDialogEvent.Show(false)) },
        title = { Text(text = stringResource(R.string.new_word_list)) },
        text = {
            OutlinedTextField(
                label = {
                    Text(text = stringResource(R.string.list_name))
                },
                isError = isError,
                value = listnameInput,
                onValueChange = {
                    onEvent(NewListDialogEvent.SetListName(it))
                },
                keyboardActions = KeyboardActions {
                    isError = listnameInput.isBlank()
                    if (!isError) {
                        onEvent(NewListDialogEvent.AddList)
                    }
                },
                trailingIcon = {
                    if (listnameInput.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable {
                                onEvent(NewListDialogEvent.SetListName(""))
                            },
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_input)
                        )
                    }
                },
                supportingText = {
                    if (isError) {
                        Text(text = stringResource(R.string.wordlist_name_must_not_be_empty))
                    }
                },
                singleLine = true,
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onEvent(NewListDialogEvent.AddList) },
                enabled = listnameInput.isNotBlank(),
                modifier = Modifier.semantics {
                    stateDescription = if (listnameInput.isBlank()) "Please enter a list name" else ""
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(NewListDialogEvent.Show(false)) },
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}





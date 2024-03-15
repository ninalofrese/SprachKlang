package br.dev.nina.sprachklang.core.presentation.components.newlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.dev.nina.sprachklang.R

@Composable
fun NewListButton(
    onEvent: (NewListDialogEvent) -> Unit
) {
    Button(
        onClick = { onEvent(NewListDialogEvent.Show(true)) },
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
        Text(text = stringResource(R.string.new_list))
    }
}

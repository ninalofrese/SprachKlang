package br.dev.nina.sprachklang.core.presentation.components.saveword

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R

@Composable
fun SaveWordButton(
    state: SaveWordState,
    onEvent: (SaveWordModalEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    FilledTonalButton(
        onClick = { onEvent(SaveWordModalEvent.Show(true)) },
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (state.selectedWordlist.isNotEmpty()) {
            Text(text = stringResource(R.string.saved))
        } else {
            Text(text = stringResource(R.string.save_to_list))
        }
        Spacer(modifier.width(4.dp))
        if (state.selectedWordlist.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null
            )
        }
    }
}

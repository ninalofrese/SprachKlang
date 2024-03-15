package br.dev.nina.sprachklang.feature_home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R

@Composable
fun SearchButton(
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { onNavigateToSearch() },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            modifier = modifier
                .widthIn(max = 320.dp)
                .semantics(mergeDescendants = true) { },
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search))
            Spacer(Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.search_cta),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

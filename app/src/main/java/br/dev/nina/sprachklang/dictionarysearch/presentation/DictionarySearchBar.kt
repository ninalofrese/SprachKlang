package br.dev.nina.sprachklang.dictionarysearch.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R

@Composable
fun SearchBar(
    state: DictionarySearchUiState,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.background, modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.query,
            onValueChange = {
                onSearchQueryChange(it)
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .semantics { traversalIndex = -1f },
            placeholder = {
                Text(text = stringResource(R.string.search_german_words))
            },
            maxLines = 1,
            singleLine = true,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (state.query.isNotBlank()) {
                    Icon(
                        modifier = Modifier.clickable {
                            onSearchQueryChange("")
                        },
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(R.string.clear_search_input)
                    )
                }
            },
        )
    }
}

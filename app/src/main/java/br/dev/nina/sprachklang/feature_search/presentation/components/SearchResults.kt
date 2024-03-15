package br.dev.nina.sprachklang.feature_search.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.core.presentation.utils.getFullPosName
import br.dev.nina.sprachklang.core.presentation.utils.localize
import br.dev.nina.sprachklang.feature_search.presentation.SearchState

@Composable
fun DictionarySearchResults(
    state: SearchState,
    loadNextItems: () -> Unit,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = modifier
    ) {
        items(state.items.size) { i ->
            val item = state.items[i]

            LaunchedEffect(scrollState) {
                if (i >= state.items.size - 1 && !state.isLoading && !state.endReached) {
                    loadNextItems()
                }
            }

            key(item.id) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .semantics(mergeDescendants = true) {}
                        .clickable { onClick(item.id) }
                ) {
                    Column {
                        Text(
                            text = item.word.localize(item.langCode),
                            style = MaterialTheme.typography.titleLarge.copy(hyphens = Hyphens.Auto),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = item.pos,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier
                                .semantics { contentDescription = getFullPosName(item.pos) }
                        )
                    }
                }
                if (i < state.items.size) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
        item {
            if (state.isLoading) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

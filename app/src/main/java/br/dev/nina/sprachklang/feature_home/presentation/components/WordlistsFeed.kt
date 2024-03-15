package br.dev.nina.sprachklang.feature_home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.presentation.components.EmptyState
import br.dev.nina.sprachklang.core.presentation.components.WordlistHeader
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.feature_home.presentation.HomeWordlistEvent
import br.dev.nina.sprachklang.feature_home.presentation.HomeWordlistState
import br.dev.nina.sprachklang.feature_home.presentation.WordlistsResult

@Composable
fun WordlistsFeed(
    feedState: HomeWordlistState,
    onItemClick: (Int) -> Unit,
    onEvent: (HomeWordlistEvent) -> Unit,
    onDialogEvent: (NewListDialogEvent) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        Surface {
            WordlistHeader(
                headerText = stringResource(R.string.your_wordlists),
                modifier = Modifier.padding(8.dp),
                trailingContent = {
                    OutlinedButton(
                        onClick = { onDialogEvent(NewListDialogEvent.Show(true)) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(stringResource(R.string.new_word_list))
                    }
                }
            )
        }
        if (feedState.wordlists.isNotEmpty()) {
            WordlistsResult(feedState, onItemClick, onEvent)
        } else {
            EmptyState(title = stringResource(R.string.wordlist_title_message), message = stringResource(R.string.worlist_empty_message)) {}
        }
    }
}

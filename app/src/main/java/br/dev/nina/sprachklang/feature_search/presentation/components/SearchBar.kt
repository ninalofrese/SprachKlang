package br.dev.nina.sprachklang.feature_search.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import br.dev.nina.sprachklang.R
import br.dev.nina.sprachklang.core.presentation.components.OutlineTextField
import kotlinx.coroutines.job

@Composable
fun SearchBar(
    query: String,
    onSearchQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        val focusRequester = remember { FocusRequester() }

        val showClearButton by remember(query) {
            derivedStateOf {
                query.isNotEmpty()
            }
        }

        Row {
            OutlineTextField(
                value = query,
                onValueChange = {
                    onSearchQueryChange(it)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .semantics {
                        traversalIndex = -1f
                    },
                placeholder = {
                    Text(text = stringResource(R.string.search_cta))
                },
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.go_back),
                        )
                    }
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = showClearButton,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(
                            onClick = onClearQuery,
                            modifier = Modifier.wrapContentSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_search_input)
                            )
                        }
                    }
                }
            )
        }
        LaunchedEffect(Unit) {
            this.coroutineContext.job.invokeOnCompletion {
                focusRequester.requestFocus()
            }
        }
    }
}

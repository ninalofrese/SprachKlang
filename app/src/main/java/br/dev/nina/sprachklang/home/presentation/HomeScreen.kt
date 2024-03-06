package br.dev.nina.sprachklang.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import br.dev.nina.sprachklang.R

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToSearch: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchButton(onNavigateToSearch, modifier = Modifier.padding(16.dp).fillMaxWidth())
    }
}

@Composable
fun SearchButton(
    onNavigateToSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { onNavigateToSearch() },
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        modifier = modifier.semantics(mergeDescendants = true) { }
    ) {
        Icon(imageVector = Icons.Default.Search, contentDescription = stringResource(R.string.search))
        Spacer(Modifier.size(8.dp))
        Text(
            text = stringResource(R.string.search_cta),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
    }
}

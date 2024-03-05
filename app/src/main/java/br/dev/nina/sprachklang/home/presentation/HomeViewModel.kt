package br.dev.nina.sprachklang.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.dictionarysearch.presentation.DictionarySearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: DictionaryRepository,
) : ViewModel() {

    val searchStateHolder = DictionarySearchState(repository, viewModelScope)
}

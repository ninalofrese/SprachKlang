package br.dev.nina.sprachklang.feature_wordlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.feature_word.presentation.audioplayer.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordlistViewModel @Inject constructor(
    private val repository: WordlistRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wordlistId = savedStateHandle.get<Int>("wordlistId")

    private val _state = MutableStateFlow(WordlistState())
    val state = _state.asStateFlow()

    private val _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    val audioPlayerManager = AudioPlayerManager()

    init {
        wordlistId?.let {
            getWordlist(it)
            getWords(it)
        }
    }

    fun onEvent(event: WordlistEvent) {
        wordlistId?.let { listId ->
            when(event) {
                is WordlistEvent.UnsaveWord -> {
                    viewModelScope.launch {
                        val wordName = state.value.entries.find { it.id == event.entryId }?.word ?: ""
                        val listName = state.value.wordlist?.name ?: ""
                        try {
                            repository.unsaveWord(wordlistId = listId, wordId = event.entryId)
                            onEvent(WordlistEvent.ShowSnackBar("Word $wordName removed from list $listName"))
                        } catch (e: Exception) {
                            onEvent(WordlistEvent.ShowSnackBar("Failed to remove word $wordName from list $listName"))
                        }
                    }
                }

                is WordlistEvent.ShowSnackBar -> {
                    viewModelScope.launch {
                        _snackBarFlow.emit(event.message)
                    }
                }
            }
        }
    }

    private fun getWordlist(listId: Int) {
        viewModelScope.launch {
            when(val result = repository.getWordlistById(listId)) {
                is Resource.Error -> {
                    _state.update { it.copy(
                        error = result.message,
                        isLoading = false,
                        wordlist = null
                    ) }
                }
                is Resource.Success -> {
                    _state.update { it.copy(
                        wordlist = result.data,
                        error = null,
                        isLoading = false
                    ) }
                }
                else -> Unit
            }
        }
    }

    private fun getWords(listId: Int) {
        viewModelScope.launch {
            repository.getWordsFromList(listId)
                .collect { result ->
                    _state.update { it.copy(
                        entries = result
                    ) }
                }
        }
    }
}

package br.dev.nina.sprachklang.word.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.word.presentation.audioplayer.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: DictionaryRepository
) : ViewModel() {

    var state by mutableStateOf(WordDetailState())
        private set

    val audioPlayerManager = AudioPlayerManager()

    init {
        getWordDetail()
    }

    fun getWordDetail() {
        viewModelScope.launch {
            val wordId = savedStateHandle.get<Int>("entryId") ?: return@launch

            val result = withContext(Dispatchers.IO){
                repository.getWordDetails(wordId)
            }

            when(result) {
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message,
                        isLoading = false,
                        entry = null
                    )
                }
                is Resource.Success -> {
                    state = state.copy(
                        entry = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                else -> Unit
            }
        }
    }
}



package br.dev.nina.sprachklang.feature_word.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordModalEvent
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordState
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.feature_word.presentation.audioplayer.AudioPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dictionaryRepository: DictionaryRepository,
    private val wordlistRepository: WordlistRepository
) : ViewModel() {
    private val entryId = savedStateHandle.get<Int>("entryId")

    private val wordDetailState = MutableStateFlow(WordDetailState())
    private val newListState = MutableStateFlow(NewListState(
        listnameInput = savedStateHandle.get<String>("listNameInput") ?: "",
        isAddingNewList = savedStateHandle.get<Boolean>("isAddingWordlist") ?: false
    ))
    private val saveWordState = MutableStateFlow(
        SaveWordState(
        isSavingWord = savedStateHandle.get<Boolean>("isSavingWord") ?: false,
    ))

    val state = combine(wordDetailState, newListState, saveWordState) { wordDetailState, newListState, addToListState ->
        WordState(
            wordDetail = wordDetailState,
            newList = newListState,
            saveWord = addToListState
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), WordState())

    val audioPlayerManager = AudioPlayerManager()

    init {
        getWordDetail()
        observeAndSaveState()
        getWordlists()
        getCurrentWordlists()
    }

    fun onDialogEvent(event: NewListDialogEvent) {
        val wordId = entryId ?: return
        when(event) {
            NewListDialogEvent.AddList -> {
                val listName = newListState.value.listnameInput
                if (listName.isBlank()) {
                    return
                }
                val wordlist = Wordlist(name = listName)

                viewModelScope.launch {
                    wordlistRepository.createWordlistAndSaveWord(wordlist, wordId)
                }
                newListState.update { it.copy(
                    isAddingNewList = false,
                    listnameInput = ""
                ) }
            }
            is NewListDialogEvent.SetListName -> {
                newListState.update { it.copy(
                    listnameInput = event.name
                ) }
            }

            is NewListDialogEvent.Show -> {
                newListState.update { it.copy(isAddingNewList = event.value) }
            }
        }
    }

    fun onModalEvent(event: SaveWordModalEvent) {
        val wordId = entryId ?: return
        when(event) {
            is SaveWordModalEvent.Show -> {
                saveWordState.update { it.copy(
                    isSavingWord = event.value
                ) }
            }
            is SaveWordModalEvent.ToggleSelectedWordlist -> {
                val isCurrentlySelected = saveWordState.value.selectedWordlist.contains(event.wordlist)
                val updatedSelection = if (isCurrentlySelected) {
                    saveWordState.value.selectedWordlist - event.wordlist
                } else {
                    saveWordState.value.selectedWordlist + event.wordlist
                }

                saveWordState.update { it.copy(selectedWordlist = updatedSelection) }

                viewModelScope.launch {
                    if (isCurrentlySelected) {
                        wordlistRepository.unsaveWord(wordId = wordId, wordlistId = event.wordlist.id)
                    } else {
                        wordlistRepository.saveWord(wordId = wordId, wordlistId = event.wordlist.id)
                    }
                }
            }
        }
    }

    private fun getWordDetail() {
        viewModelScope.launch {
            val wordId = entryId ?: return@launch

            when(val result = dictionaryRepository.getWordDetails(wordId)) {
                is Resource.Error -> {
                    wordDetailState.update { it.copy(
                        error = result.message,
                        isLoading = false,
                        entry = null
                    ) }
                }
                is Resource.Success -> {
                    wordDetailState.update { it.copy(
                        entry = result.data,
                        isLoading = false,
                        error = null
                    ) }
                }
                else -> Unit
            }
        }
    }

    private fun getCurrentWordlists() {
        viewModelScope.launch {
            val wordId = entryId ?: return@launch
            wordlistRepository.getWordlistByWordId(wordId)
                .collect { result ->
                    saveWordState.update { it.copy(
                        selectedWordlist = result
                    ) }
                }
        }
    }

    private fun getWordlists() {
        viewModelScope.launch {
            wordlistRepository.getWordlists()
                .collect { result ->
                    saveWordState.update { it.copy(
                        wordlists = result
                    ) }
                }
        }
    }

    private fun observeAndSaveState() {
        newListState.map { it.listnameInput }.distinctUntilChanged().onEach {
            savedStateHandle["listNameInput"] = it
        }.launchIn(viewModelScope)

        newListState.map { it.isAddingNewList }.distinctUntilChanged().onEach {
            savedStateHandle["isAddingWordlist"] = it
        }.launchIn(viewModelScope)

        saveWordState.map { it.isSavingWord }.distinctUntilChanged().onEach {
            savedStateHandle["isSavingWord"] = it
        }.launchIn(viewModelScope)
    }
}

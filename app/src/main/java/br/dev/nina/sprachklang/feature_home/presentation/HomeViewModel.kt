package br.dev.nina.sprachklang.feature_home.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wordlistRepository: WordlistRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val newListState = combine(
        savedStateHandle.getStateFlow(LISTNAME_INPUT, ""),
        savedStateHandle.getStateFlow(IS_ADDING_LIST, false)
    ) { nameInput, isAddingList ->
        savedStateHandle[LISTNAME_INPUT] = nameInput
        savedStateHandle[IS_ADDING_LIST] = isAddingList

        NewListState(
            isAddingNewList = isAddingList,
            listnameInput = nameInput
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NewListState()
    )

    val wordlistState: StateFlow<HomeWordlistState> =
        wordlistRepository.getWordlists()
            .map { HomeWordlistState(it, isLoading = false) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeWordlistState(isLoading = true)
            )

    private val _snackBarFlow = MutableSharedFlow<String>()
    val snackBarFlow = _snackBarFlow.asSharedFlow()

    fun onEvent(event: HomeWordlistEvent) {
        when (event) {
            is HomeWordlistEvent.DeleteWordList -> {
                viewModelScope.launch {
                    try {
                        wordlistRepository.deleteWordlist(event.wordlist)
                        onEvent(HomeWordlistEvent.ShowSnackbar("Wordlist ${event.wordlist.name} removed successfully"))
                    } catch (e: Exception) {
                        onEvent(HomeWordlistEvent.ShowSnackbar("Failed to remove wordlist ${event.wordlist.name}"))
                    }
                }
            }

            is HomeWordlistEvent.ShowSnackbar -> {
                viewModelScope.launch {
                    _snackBarFlow.emit(event.message)
                }
            }
        }
    }

    fun onDialogEvent(event: NewListDialogEvent) {
        when(event) {
            NewListDialogEvent.AddList -> {
                val listName = newListState.value.listnameInput
                if (listName.isBlank()) {
                    return
                }
                val wordlist = Wordlist(name = listName)
                onDialogEvent(NewListDialogEvent.Show(false))
                viewModelScope.launch {
                    try {
                        wordlistRepository.createWordlist(wordlist)
                        onEvent(HomeWordlistEvent.ShowSnackbar("Wordlist $listName added successfully"))
                    } catch (e: Exception) {
                        onEvent(HomeWordlistEvent.ShowSnackbar("Failed to add wordlist $listName"))
                    }
                }
            }
            is NewListDialogEvent.SetListName -> {
                savedStateHandle[LISTNAME_INPUT] = event.name
            }

            is NewListDialogEvent.Show -> {
                savedStateHandle[IS_ADDING_LIST] = event.value
                savedStateHandle[LISTNAME_INPUT] = if (!event.value) "" else newListState.value.listnameInput
            }
        }
    }
}

private const val LISTNAME_INPUT = "listNameInput"
private const val IS_ADDING_LIST = "isAddingWordlist"

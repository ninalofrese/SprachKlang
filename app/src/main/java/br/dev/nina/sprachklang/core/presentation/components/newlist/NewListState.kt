package br.dev.nina.sprachklang.core.presentation.components.newlist

data class NewListState(
    val isAddingNewList: Boolean = false,
    val listnameInput: String = ""
)

sealed interface NewListDialogEvent {
    data class SetListName(val name: String) : NewListDialogEvent
    object AddList : NewListDialogEvent
    data class Show(val value: Boolean): NewListDialogEvent
}

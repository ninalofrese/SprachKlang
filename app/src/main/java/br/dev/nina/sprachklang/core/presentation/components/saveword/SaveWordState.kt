package br.dev.nina.sprachklang.core.presentation.components.saveword

import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist

data class SaveWordState(
    val isSavingWord: Boolean = false,
    val wordlists: List<Wordlist> = emptyList(),
    val selectedWordlist: List<Wordlist> = emptyList(),
)

sealed interface SaveWordModalEvent {
    data class Show(val value: Boolean): SaveWordModalEvent
    data class ToggleSelectedWordlist(val wordlist: Wordlist): SaveWordModalEvent
}

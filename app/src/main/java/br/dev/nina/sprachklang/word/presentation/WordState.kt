package br.dev.nina.sprachklang.word.presentation

import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry

data class WordDetailState(
    val entry: Entry? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class AudioPlayerEvent {
    data class OnAudioPrepare(val url: String): AudioPlayerEvent()
    object OnAudioStop: AudioPlayerEvent()
//    object OnLoadNextItems: WordDetailEvent()
}

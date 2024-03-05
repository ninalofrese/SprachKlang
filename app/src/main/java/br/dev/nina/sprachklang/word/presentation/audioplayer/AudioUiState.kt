package br.dev.nina.sprachklang.word.presentation.audioplayer

data class AudioUiState(
    val audioUrls: List<String> = emptyList(),
    val word: String = "",
    val langCode: String = ""
)

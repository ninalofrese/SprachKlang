package br.dev.nina.sprachklang.feature_word.presentation.audioplayer

data class AudioState(
    val audioUrls: List<String> = emptyList(),
    val word: String = "",
    val langCode: String = ""
)

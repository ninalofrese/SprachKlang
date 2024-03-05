package br.dev.nina.sprachklang.core.domain.dictionary.model

data class Entry(
    val id: Int,
    val word: String,
    val langCode: String,
    val pos: String,
    val ipas: List<String>,
    val audioUrls: List<String>?,
    val definitions: List<Definition>
)

package br.dev.nina.sprachklang.dictionarysearch.domain.model

data class Entry(
    val id: Int,
    val word: String,
    val pos: String,
    val ipas: List<String>,
    val audioUrls: List<String>?,
    val definitions: List<Definition>
)

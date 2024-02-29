package br.dev.nina.sprachklang.dictionarysearch.domain.model

data class Headword(
    val id: Int,
    val word: String,
    val pos: String,
    val langCode: String
)

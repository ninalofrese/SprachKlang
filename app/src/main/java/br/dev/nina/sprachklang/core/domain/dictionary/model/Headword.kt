package br.dev.nina.sprachklang.core.domain.dictionary.model

data class Headword(
    val id: Int,
    val word: String,
    val pos: String,
    val langCode: String
)

package br.dev.nina.sprachklang.core.domain.dictionary.model

data class Definition(
    val glosses: List<String>?,
    val synonyms: List<String>?,
    val related: List<String>?
)

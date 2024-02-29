package br.dev.nina.sprachklang.dictionarysearch.domain.model

data class Definition(
    val glosses: List<String>?,
    val synonyms: List<String>?,
    val related: List<String>?
)

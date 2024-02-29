package br.dev.nina.sprachklang.core.util

val Any.tag: String
    get() = this::class.java.simpleName ?: "N/A"

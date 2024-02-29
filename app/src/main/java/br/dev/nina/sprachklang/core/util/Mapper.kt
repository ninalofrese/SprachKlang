package br.dev.nina.sprachklang.core.util

interface Mapper<in I, out O> {
    fun map(input: I): O
}

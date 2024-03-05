package br.dev.nina.sprachklang.core.util.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems(query: String)
    suspend fun reset()
}

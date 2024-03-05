package br.dev.nina.sprachklang.core.util.pagination

import br.dev.nina.sprachklang.core.util.Resource

class QueryPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key, query: String) -> Resource<List<Item>>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (String?) -> Unit,
    private inline val onSuccess: suspend (newItems: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems(query: String) {
        if (query.isBlank()) return onSuccess(emptyList(), currentKey)
        if (isMakingRequest) return

        isMakingRequest = true
        onLoadUpdated(true)
        when (val result = onRequest(currentKey, query)) {
            is Resource.Success -> {
                val items = result.data ?: emptyList()
                currentKey = getNextKey(items)
                onSuccess(items, currentKey)
                isMakingRequest = false
                onLoadUpdated(false)
            }
            is Resource.Error -> {
                onError(result.message)
                isMakingRequest = false
                onLoadUpdated(false)
            }
            is Resource.Loading -> Unit
        }
    }

    override suspend fun reset() {
        currentKey = initialKey
    }
}

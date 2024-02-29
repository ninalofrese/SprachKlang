package br.dev.nina.sprachklang.dictionarysearch.domain

import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.dictionarysearch.domain.model.Entry
import br.dev.nina.sprachklang.dictionarysearch.domain.model.Headword
import kotlinx.coroutines.flow.Flow

interface DictionaryRepository {
    fun searchWordWithDefinition(query: String, page: Int, pageSize: Int): Flow<Resource<List<Entry>>>
    fun searchWord(query: String, page: Int, pageSize: Int): Flow<Resource<List<Headword>>>
    fun getWordDetails(wordId: Int): Flow<Resource<Entry>>
}

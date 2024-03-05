package br.dev.nina.sprachklang.core.domain.dictionary

import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword

interface DictionaryRepository {
    suspend fun searchWordWithDefinition(query: String, page: Int, pageSize: Int): Resource<List<Entry>>
    suspend fun searchWord(query: String, page: Int, pageSize: Int): Resource<List<Headword>>
    suspend fun getWordDetails(wordId: Int): Resource<Entry>
}

package br.dev.nina.sprachklang.core.domain.dictionary

import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword
import br.dev.nina.sprachklang.core.util.Resource

interface DictionaryRepository {
    suspend fun searchWord(query: String, page: Int, pageSize: Int): Resource<List<Headword>>
    suspend fun getWordDetails(wordId: Int): Resource<Entry>
}

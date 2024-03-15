package br.dev.nina.sprachklang.core.data.dictionary.testdoubles

import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword
import br.dev.nina.sprachklang.core.util.Resource

class FakeDictionaryRepository : DictionaryRepository {

    private val headwords = mutableListOf<Headword>()
    private val entries = mutableListOf<Entry>()

    override suspend fun searchWord(query: String, page: Int, pageSize: Int): Resource<List<Headword>> {
        return Resource.Success(
            data = headwords
        )
    }

    override suspend fun getWordDetails(wordId: Int): Resource<Entry> {
        val entry = entries.find { wordId == it.id }
        return Resource.Success(
            data = entry
        )
    }

}

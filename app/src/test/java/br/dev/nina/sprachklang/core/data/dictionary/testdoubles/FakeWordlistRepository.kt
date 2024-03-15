package br.dev.nina.sprachklang.core.data.dictionary.testdoubles

import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Definition
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeWordlistRepository: WordlistRepository {

    private val entries = entriesTestData
    private val wordlists = mutableListOf<Wordlist>()
    private val savedWordlist = mutableMapOf<Int, Int>()

    override fun getWordlists(): Flow<List<Wordlist>> {
        return flow { wordlists }
    }

    override suspend fun getWordlistById(wordlistId: Int): Resource<Wordlist> {
        val wordlist = wordlists.find { it.id == wordlistId }
        return Resource.Success(
            data = wordlist
        )
    }

    override fun getWordsFromList(wordlistId: Int): Flow<List<Entry>> {
        val savedWords = savedWordlist.filterValues { it == wordlistId }
        val foundEntries = savedWords.flatMap { saved ->
            entries.filter { it.id == saved.key }
        }
        return flowOf(foundEntries)
    }

    override suspend fun createWordlist(wordlist: Wordlist) {
        wordlists.add(wordlist)
    }

    override suspend fun createWordlistAndSaveWord(wordlist: Wordlist, wordId: Int) {
        wordlists.add(wordlist)
        savedWordlist[wordId] = wordlist.id
    }

    override suspend fun deleteWordlist(wordlist: Wordlist) {
        wordlists.remove(wordlist)
    }

    override suspend fun saveWord(wordlistId: Int, wordId: Int) {
        savedWordlist[wordId] = wordlistId
    }

    override suspend fun unsaveWord(wordlistId: Int, wordId: Int) {
        savedWordlist.remove(wordId,wordlistId)
    }

    override fun getWordlistByWordId(wordId: Int): Flow<List<Wordlist>> {
        return flow { wordlists.find { wordId == it.id } }
    }

}

val entriesTestData = (1..10).map {
    Entry(
        id = it,
        word = "genau",
        langCode = "de",
        pos = "adv",
        ipas = emptyList(),
        audioUrls = null,
        definitions = listOf(
            Definition(
                glosses = listOf("exact", "exactly"),
                synonyms = null,
                related = null
            )
        )
    )
}

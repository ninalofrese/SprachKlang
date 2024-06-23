package br.dev.nina.sprachklang.core.data.testdoubles

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
    val wordlists = mutableListOf<Wordlist>()
    private val savedWordlist = mutableMapOf<Int, Int>()
    var createWordlistCalled = false
    var deleteWordlistCalled = false
    var saveWordCalled = false
    var createWordlistAndSaveWordCalled = false
    var unsaveWordCalled = false
    var unsaveWordId = -1
    var isError = false

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
        createWordlistCalled = true
        if (isError) throw Exception("Failed to add")
        wordlists.add(wordlist)
    }

    override suspend fun createWordlistAndSaveWord(wordlist: Wordlist, wordId: Int) {
        createWordlistAndSaveWordCalled = true
        wordlists.add(wordlist)
        savedWordlist[wordId] = wordlist.id
    }

    override suspend fun deleteWordlist(wordlist: Wordlist) {
        deleteWordlistCalled = true
        if (isError) throw Exception("Failed to delete")
        wordlists.remove(wordlist)
    }

    override suspend fun saveWord(wordlistId: Int, wordId: Int) {
        saveWordCalled = true
        if (isError) throw Exception("Failed to save word")
        savedWordlist[wordId] = wordlistId
    }

    override suspend fun unsaveWord(wordlistId: Int, wordId: Int) {
        unsaveWordCalled = true
        unsaveWordId = wordId
        if (isError) throw Exception("Failed to unsave word")
        savedWordlist.remove(wordId,wordlistId)
    }

    override fun getWordlistByWordId(wordId: Int): Flow<List<Wordlist>> {
        return flow { wordlists.find { wordId == it.id } }
    }

    fun reset() {
        createWordlistCalled = false
        deleteWordlistCalled = false
        createWordlistAndSaveWordCalled = false
        saveWordCalled = false
        unsaveWordCalled = false
        unsaveWordId = -1
        isError = false
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

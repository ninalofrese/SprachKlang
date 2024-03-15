package br.dev.nina.sprachklang.core.domain.dictionary

import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.util.Resource
import kotlinx.coroutines.flow.Flow

interface WordlistRepository {
    suspend fun createWordlist(wordlist: Wordlist)
    fun getWordlists(): Flow<List<Wordlist>>
    suspend fun getWordlistById(wordlistId: Int): Resource<Wordlist>
    suspend fun deleteWordlist(wordlist: Wordlist)
    suspend fun createWordlistAndSaveWord(wordlist: Wordlist, wordId: Int)

    suspend fun saveWord(wordlistId: Int, wordId: Int)
    suspend fun unsaveWord(wordlistId: Int, wordId: Int)
    fun getWordsFromList(wordlistId: Int): Flow<List<Entry>>
    fun getWordlistByWordId(wordId: Int): Flow<List<Wordlist>>
}

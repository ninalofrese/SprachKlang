package br.dev.nina.sprachklang.core.data

import android.database.sqlite.SQLiteException
import android.util.Log
import br.dev.nina.sprachklang.core.data.database.WordlistDao
import br.dev.nina.sprachklang.core.data.database.entities.SavedWordCrossRef
import br.dev.nina.sprachklang.core.data.database.mappers.asEntries
import br.dev.nina.sprachklang.core.data.database.mappers.asWordlist
import br.dev.nina.sprachklang.core.data.database.mappers.asWordlistEntity
import br.dev.nina.sprachklang.core.data.database.mappers.asWordlists
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.util.DispatcherProvider
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.core.util.tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordlistRepositoryImpl @Inject constructor(
    private val dao: WordlistDao,
    private val dispatcher: DispatcherProvider
): WordlistRepository {

    override fun getWordlists(): Flow<List<Wordlist>> =
        dao.getCollections().map { it.asWordlists() }


    override fun getWordsFromList(wordlistId: Int): Flow<List<Entry>> {
        val result = try {
            dao.getWordsByListId(wordlistId)
        } catch (e: SQLiteException) {
            Log.e(this@WordlistRepositoryImpl.tag, e.message, e)
            flowOf(emptyList())
        }

        return result.mapNotNull { it.asEntries() }
    }

    override suspend fun createWordlist(wordlist: Wordlist) {
        withContext(dispatcher.io) {
            dao.insertWordlist(wordlist.asWordlistEntity())
        }
    }

    override suspend fun createWordlistAndSaveWord(wordlist: Wordlist, wordId: Int) {
        withContext(dispatcher.io) {
            dao.createWordlistWithWord(wordId = wordId, wordlist = wordlist.asWordlistEntity())
        }
    }

    override suspend fun deleteWordlist(wordlist: Wordlist) {
        withContext(dispatcher.io) {
            dao.deleteWordlist(wordlist.id)
        }
    }

    override suspend fun getWordlistById(wordlistId: Int): Resource<Wordlist> = withContext(dispatcher.io) {
        try {
            val localWordlist = withContext(dispatcher.io) {
                dao.getWordlistById(wordlistId)
            }
            val wordlist = localWordlist.asWordlist()
            Resource.Success(
                data = wordlist
            )
        } catch (e: SQLiteException) {
            Log.e(this@WordlistRepositoryImpl.tag, e.message, e)
            Resource.Error(message = "Couldn't load data.")
        }
    }

    override fun getWordlistByWordId(wordId: Int): Flow<List<Wordlist>> {
        return try {
            dao.getWordlistsForWord(wordId).mapNotNull { it.asWordlists() }
        } catch (e: SQLiteException) {
            Log.e(this@WordlistRepositoryImpl.tag, e.message, e)
            flowOf(emptyList())
        }
    }

    override suspend fun saveWord(wordlistId: Int, wordId: Int) {
        withContext(dispatcher.io) {
            dao.saveWord(
                SavedWordCrossRef(wordId = wordId, wordlistId = wordlistId)
            )
        }
    }

    override suspend fun unsaveWord(wordlistId: Int, wordId: Int) {
        withContext(dispatcher.io) {
            dao.unsaveWord(
                SavedWordCrossRef(wordId = wordId, wordlistId = wordlistId)
            )
        }
    }
}

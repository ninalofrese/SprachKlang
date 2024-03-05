package br.dev.nina.sprachklang.core.data.dictionary

import android.database.sqlite.SQLiteException
import android.util.Log
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.core.util.tag
import br.dev.nina.sprachklang.core.data.dictionary.db.DictionaryDatabase
import br.dev.nina.sprachklang.core.data.dictionary.db.asEntries
import br.dev.nina.sprachklang.core.data.dictionary.db.asEntry
import br.dev.nina.sprachklang.core.data.dictionary.db.asHeadword
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryDatabase: DictionaryDatabase
) : DictionaryRepository {

    private val dao = dictionaryDatabase.dao

    override suspend fun searchWordWithDefinition(query: String, page: Int, pageSize: Int): Resource<List<Entry>> {
        return try {
            val offset = page * pageSize
            val localEntries = dao.searchWordWithDefinition(query, pageSize, offset)
            if (offset + pageSize <= localEntries.size) {
                Resource.Success(
                    data = localEntries.asEntries()
                )
            } else {
                Resource.Success(emptyList())
            }
        } catch (e: SQLiteException) {
            Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
            Resource.Error(message = "Couldn't load data.")
        }
    }

    override suspend fun searchWord(query: String, page: Int, pageSize: Int): Resource<List<Headword>> {
        return try {
            val offset = page * pageSize
            val localEntries = dao.searchWord(query, pageSize, offset)
            if (localEntries.isNotEmpty()) {
                Resource.Success(
                    data = localEntries.map {
                        it.asHeadword()
                    }
                )
            } else {
                Resource.Success(emptyList())
            }

        } catch (e: SQLiteException) {
            Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
            Resource.Error(message = "Couldn't load data.")
        }
    }


    override suspend fun getWordDetails(wordId: Int): Resource<Entry> {
        return try {
            val localEntries = dao.getWordDetails(wordId)
            val entry = localEntries.asEntry()
            if (entry != null) {
                Resource.Success(
                    data = entry
                )
            } else {
                Resource.Error(message = "Couldn't find data.")
            }
        } catch (e: SQLiteException) {
            Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
            Resource.Error(message = "Couldn't load data.")
        }
    }
}

package br.dev.nina.sprachklang.dictionarysearch.data

import android.database.sqlite.SQLiteException
import android.util.Log
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.core.util.tag
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.DictionaryDatabase
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.asEntries
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.asEntry
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.asHeadword
import br.dev.nina.sprachklang.dictionarysearch.domain.DictionaryRepository
import br.dev.nina.sprachklang.dictionarysearch.domain.model.Entry
import br.dev.nina.sprachklang.dictionarysearch.domain.model.Headword
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepositoryImpl @Inject constructor(
    private val dictionaryDatabase: DictionaryDatabase
): DictionaryRepository {

    private val dao = dictionaryDatabase.dao

    override fun searchWordWithDefinition(query: String, page: Int, pageSize: Int): Flow<Resource<List<Entry>>> {
        return flow {
            emit(Resource.Loading(true))
            val offset = page * pageSize
            try {
                val localEntries = dao.searchWordWithDefinition(query, pageSize, offset)
                emit(Resource.Loading(false))
                emit(Resource.Success(
                    data = localEntries.asEntries()
                ))
            } catch (e: SQLiteException) {
                Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
                emit(Resource.Error(message = "Couldn't load data."))
            }
        }
    }

    override fun searchWord(query: String, page: Int, pageSize: Int): Flow<Resource<List<Headword>>> {
        return flow {
            emit(Resource.Loading(true))
            val offset = page * pageSize
            try {
                val localEntries = dao.searchWord(query, pageSize, offset)
                emit(Resource.Loading(false))
                emit(Resource.Success(
                    data = localEntries.map {
                        it.asHeadword()
                    }
                ))
            } catch (e: SQLiteException) {
                Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
                emit(Resource.Error(message = "Couldn't load data."))
            }
        }
    }

    override fun getWordDetails(wordId: Int): Flow<Resource<Entry>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val localEntries = dao.getWordDetails(wordId)
                emit(Resource.Loading(false))
                val entry = localEntries.asEntry()
                if (entry != null) {
                    emit(Resource.Success(
                        data = entry
                    ))
                } else {
                    emit(Resource.Error(message = "Couldn't find data."))
                }
            } catch (e: SQLiteException) {
                Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
                emit(Resource.Error(message = "Couldn't load data."))
            }
        }
    }
}

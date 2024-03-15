package br.dev.nina.sprachklang.core.data

import android.database.sqlite.SQLiteException
import android.util.Log
import br.dev.nina.sprachklang.core.data.database.DictionaryDao
import br.dev.nina.sprachklang.core.data.database.mappers.asEntries
import br.dev.nina.sprachklang.core.data.database.mappers.asHeadword
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Entry
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword
import br.dev.nina.sprachklang.core.util.DispatcherProvider
import br.dev.nina.sprachklang.core.util.Resource
import br.dev.nina.sprachklang.core.util.tag
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DictionaryRepositoryImpl @Inject constructor(
    private val dao: DictionaryDao,
    private val dispatcher: DispatcherProvider
) : DictionaryRepository {



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
            val result = withContext(dispatcher.io) {
                dao.getWordDetails(wordId)
                    .asEntries()
                    .singleOrNull()
            }

            Resource.Success(
                data = result
            )
        } catch (e: SQLiteException) {
            Log.e(this@DictionaryRepositoryImpl.tag, e.message, e)
            Resource.Error(message = "Couldn't load data.")
        }
    }
}

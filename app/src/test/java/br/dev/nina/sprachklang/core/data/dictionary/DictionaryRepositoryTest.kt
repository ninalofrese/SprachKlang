package br.dev.nina.sprachklang.core.data.dictionary

import android.database.sqlite.SQLiteException
import br.dev.nina.sprachklang.core.MainDispatcherRule
import br.dev.nina.sprachklang.core.TestDispatchers
import br.dev.nina.sprachklang.core.data.DictionaryRepositoryImpl
import br.dev.nina.sprachklang.core.data.database.DictionaryDao
import br.dev.nina.sprachklang.core.data.dictionary.testdoubles.dummyEntryResults
import br.dev.nina.sprachklang.core.data.dictionary.testdoubles.dummyHeadwordEntity
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Headword
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DictionaryRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testDispatchers: TestDispatchers
    private lateinit var dao: DictionaryDao
    private lateinit var repository: DictionaryRepository

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        dao = mockk()
        repository = DictionaryRepositoryImpl(
            dispatcher = testDispatchers,
            dao = dao
        )
    }

    @Test
    fun searchWord_ReturnList() {
        coEvery { dao.searchWord(any(), any(), any()) }.returns(
            listOf(dummyHeadwordEntity(32))
        )

        val actual = runBlocking {
            repository.searchWord("genau", page = 0, pageSize = 20)
        }

        assertThat(actual.data).containsExactly(
            Headword(
                id = 32,
                pos = "adv",
                word = "genau",
                langCode = "de"
            )
        )
    }

    @Test
    fun searchWord_ReturnEmptyList() {
        coEvery { dao.searchWord(any(), any(), any()) }.returns(
            emptyList()
        )

        val actual = runBlocking {
            repository.searchWord("", page = 0, pageSize = 20)
        }

        assertThat(actual.data).isEmpty()
    }

    @Test
    fun searchWord_ReturnError() {
        coEvery { dao.searchWord(any(), any(), any()) }.throws(
            SQLiteException()
        )

        val actual = runBlocking {
            repository.searchWord("", page = 1, pageSize = 20)
        }

        assertThat(actual.data).isNull()
        assertThat(actual.message).isNotNull()
    }

    @Test
    fun getWordDetails_ReturnFlattenedList() = runTest {
        val dummyEntryResults = dummyEntryResults(size = 2, wordId = 32)
        coEvery { dao.getWordDetails(any()) }.returns(dummyEntryResults)

        val actual = repository.getWordDetails(32)

        assertThat(actual.data).isNotNull()
        assertThat(actual.data?.definitions).hasSize(2)
    }

    @Test
    fun getWordDetails_ReturnError() = runTest {
        coEvery { dao.getWordDetails(any()) }.throws(SQLiteException())

        val actual = repository.getWordDetails(32)

        assertThat(actual.data).isNull()
        assertThat(actual.message).isNotNull()
    }

    @Test
    fun getWordDetails_WhenDaoResultMultipleEntryResultsReturnNull() = runTest {
        val dummyEntryResults = dummyEntryResults(wordId = 34) + dummyEntryResults(wordId = 12)
        coEvery { dao.getWordDetails(any()) }.returns(dummyEntryResults)

        val actual = repository.getWordDetails(12)

        assertThat(actual.data).isNull()
    }
}

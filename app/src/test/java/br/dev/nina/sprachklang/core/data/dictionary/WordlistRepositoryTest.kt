package br.dev.nina.sprachklang.core.data.dictionary

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import br.dev.nina.sprachklang.core.MainDispatcherRule
import br.dev.nina.sprachklang.core.TestDispatchers
import br.dev.nina.sprachklang.core.data.WordlistRepositoryImpl
import br.dev.nina.sprachklang.core.data.database.WordlistDao
import br.dev.nina.sprachklang.core.data.database.entities.WordlistEntity
import br.dev.nina.sprachklang.core.data.dictionary.testdoubles.dummyEntryResults
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WordlistRepositoryTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var testDispatchers: TestDispatchers
    private lateinit var dao: WordlistDao
    private lateinit var repository: WordlistRepositoryImpl

    @Before
    fun setUp() {
        testDispatchers = TestDispatchers()
        dao = mockk()
        repository = WordlistRepositoryImpl(
            dispatcher = testDispatchers,
            dao = dao
        )
    }

    @Test
    fun getWordlists_ReturnList() = runTest {
        coEvery { dao.getCollections() }.returns(flowOf(listOf(WordlistEntity(name = "Animals"))))

        val actual = repository.getWordlists()

        assertThat(actual.first()).hasSize(1)
    }

    @Test
    fun getWordlists_ReturnEmpty() = runTest {
        coEvery { dao.getCollections() }.returns(flowOf(emptyList()))

        val actual = repository.getWordlists()

        assertThat(actual.first()).isEmpty()
    }

    @Test
    fun getWordsFromList_ReturnList() = runTest {
        val dummyEntryResults = dummyEntryResults(2, 12) + dummyEntryResults(1, 5)
        coEvery { dao.getWordsByListId(any()) }.returns(flowOf(dummyEntryResults))

        val actual = repository.getWordsFromList(1)

        assertThat(actual.first()).hasSize(2)
    }

    @Test
    fun getWordsFromList_ReturnEmpty() = runTest {
        coEvery { dao.getWordsByListId(any()) }.returns(flowOf(emptyList()))

        val actual = repository.getWordsFromList(1)

        assertThat(actual.first()).isEmpty()
    }

    @Test
    fun getWordsFromList_WhenSQLiteExceptionReturnEmpty() = runTest {
        coEvery { dao.getWordsByListId(any()) }.throws(SQLiteConstraintException())

        val actual = repository.getWordsFromList(1)

        assertThat(actual.first()).isEmpty()
    }

    @Test
    fun getWordlistById_ReturnList() = runTest {
        coEvery { dao.getWordlistById(any()) }.returns(WordlistEntity(13, "Animals"))

        val actual = repository.getWordlistById(13)

        assertThat(actual.data).isEqualTo(Wordlist(13, "Animals"))
    }

    @Test
    fun getWordlistById_ReturnError() = runTest {
        coEvery { dao.getWordlistById(any()) }.throws(SQLiteException())

        val actual = repository.getWordlistById(13)

        assertThat(actual.data).isNull()
        assertThat(actual.message).isNotNull()
    }

    @Test
    fun getWordlistByWordId_ReturnList() = runTest {
        coEvery { dao.getWordlistsForWord(any()) }.returns(flowOf(listOf(WordlistEntity(13, "Animals"))))

        val actual = repository.getWordlistByWordId(13)

        assertThat(actual.first()).containsExactly(
            Wordlist(id = 13, name = "Animals")
        )
    }

    @Test
    fun getWordlistByWordId_ReturnError() = runTest {
        coEvery { dao.getWordlistsForWord(any()) }.throws(SQLiteException())

        val actual = repository.getWordlistByWordId(13)

        assertThat(actual.first()).isEmpty()
    }
}

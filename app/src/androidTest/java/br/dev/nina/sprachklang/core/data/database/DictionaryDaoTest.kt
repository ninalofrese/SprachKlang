package br.dev.nina.sprachklang.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ApplicationProvider
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.definition1
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.definition1_2
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.definition1_3
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.definition2
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.definition3
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.definitionInsert
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.headword1
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.headword2
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.headword3
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.headwordInsert
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DictionarDaoTest {

    private lateinit var dictionaryDao: DictionaryDao
    private lateinit var db: SupportSQLiteDatabase
    private lateinit var dictDb: DictionaryDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dictDb = Room.inMemoryDatabaseBuilder(
            context,
            DictionaryDatabase::class.java
        ).build()
        db = dictDb.openHelper.writableDatabase
        dictionaryDao = dictDb.dictionaryDao

        populateDb()
    }

    @After
    fun tearDown() {
        db.close()
        dictDb.close()
    }

    private fun populateDb() {

        db.beginTransaction()
        try {

            db.execSQL(sql = headwordInsert, headword1)
            db.execSQL(sql = headwordInsert, headword2)
            db.execSQL(sql = headwordInsert, headword3)
            db.execSQL(sql = definitionInsert, definition1)
            db.execSQL(sql = definitionInsert, definition1_2)
            db.execSQL(sql = definitionInsert, definition1_3)
            db.execSQL(sql = definitionInsert, definition2)
            db.execSQL(sql = definitionInsert, definition3)

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    @Test
    fun searchWord_OrderByWordAscending() = runTest {

        val headwords = dictionaryDao.searchWord("ab", limit = 20, offset = 0)

        assertThat(
            headwords.map { it.word }
        ).containsExactly("abdominales", "aboral").inOrder()
    }

    @Test
    fun getWordDetails_ReturnAtMostOneEntry() = runTest {
        val entries = dictionaryDao.getWordDetails(11)

        assertThat(entries.size).isAtMost(1)
    }

    @Test
    fun getWordDetails_ReturnAllDefinitions() = runTest {
        val entries = dictionaryDao.getWordDetails(11)


        val definitions = entries.flatMap { it.definitions }
        assertThat(definitions).hasSize(3)
    }
}

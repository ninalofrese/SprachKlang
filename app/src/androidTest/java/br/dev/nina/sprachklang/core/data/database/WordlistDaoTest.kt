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
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.headword4
import br.dev.nina.sprachklang.core.PrePopulatedDictionary.headwordInsert
import br.dev.nina.sprachklang.core.data.database.entities.SavedWordCrossRef
import br.dev.nina.sprachklang.core.data.database.entities.WordlistEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class WordlistDaoTest {
    private lateinit var wordlistDao: WordlistDao
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
        wordlistDao = dictDb.wordlistDao

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
            db.execSQL(sql = headwordInsert, headword4)
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
    fun deleteWordlist_NotBeInCollections() = runTest {
        val insertId = givenInsertedWordlist("B2 Exam")

        wordlistDao.deleteWordlist(insertId)

        assertThat(insertId).isNotIn(wordlistDao.getCollections().first().map { it.id })
    }

    @Test
    fun getWordlistById_ReturnEntity() = runTest {
        val insertId = givenInsertedWordlist("Colors")

        val actual = wordlistDao.getWordlistById(insertId)

        assertThat(actual).isEqualTo(WordlistEntity(insertId, "Colors"))
    }

    @Test
    fun getCollections_ReturnAllWordlists() = runTest {
        ('A'.. 'Z').map {
            givenInsertedWordlist("$it list")
        }

        val actual = wordlistDao.getCollections()

        assertThat(actual.first()).hasSize(26)
    }

    @Test
    fun getWordlistsForWord_ReturnWordlist() = runTest {
        val insertedIds = ('A'.. 'Z').map {
            givenInsertedWordlist("$it list")
        }
        givenSavedWord(wordId = 15, wordlistId = insertedIds[2])
        givenSavedWord(wordId = 15, wordlistId = insertedIds[7])

        val actual = wordlistDao.getWordlistsForWord(15)

        assertThat(actual.first().map { it.id }).containsExactly(insertedIds[2], insertedIds[7])
    }

    @Test
    fun getWordlistsForWord_noWordlists() = runTest {
        val actual = wordlistDao.getWordlistsForWord(15)

        assertThat(actual.first().map { it.id }).isEmpty()
    }

    @Test
    fun getWordsByListId_ReturnUnflattenedWords() = runTest {
        val insertedId = givenInsertedWordlist("Education")
        givenSavedWord(wordId = 11, wordlistId = insertedId)
        givenSavedWord(wordId = 12, wordlistId = insertedId)
        givenSavedWord(wordId = 15, wordlistId = insertedId)

        val actual = wordlistDao.getWordsByListId(insertedId)

        assertThat(actual.first().map { it.headword.id }).containsAtLeast(11, 12, 15)
    }

    @Test
    fun getWordsByListId_NoWords() = runTest {
        val insertedId = givenInsertedWordlist("Education")
        givenSavedWord(wordId = 11, wordlistId = insertedId)
        givenUnsavedWord(wordId = 11, wordlistId = insertedId)

        val actual = wordlistDao.getWordsByListId(insertedId)

        assertThat(actual.first()).isEmpty()
    }

    @Test
    fun getWordsByListId_ReturnWordsWithAllDefinitions() = runTest {
        val insertedId = givenInsertedWordlist("Education")
        givenSavedWord(wordId = 11, wordlistId = insertedId)

        val actual = wordlistDao.getWordsByListId(insertedId)

        assertThat(actual.first().map { it.definitions }).hasSize(3)
    }

    @Test
    fun createWordlistWithWord_InCollections() = runTest {
        val wordlistEntity = WordlistEntity(name = "B2 Exam")

        wordlistDao.createWordlistWithWord(wordId = 12, wordlistEntity)

        assertThat("B2 Exam").isIn(wordlistDao.getWordlistsForWord(12).first().map { it.name })
    }

    private fun givenInsertedWordlist(name: String): Int = runBlocking {
        val wordlist = WordlistEntity(name = name)

        wordlistDao.insertWordlist(wordlist).toInt()
    }

    private fun givenSavedWord(wordId: Int, wordlistId: Int) = runBlocking {
        val crossRef = SavedWordCrossRef(wordId = wordId, wordlistId = wordlistId)

        wordlistDao.saveWord(crossRef)
    }

    private fun givenUnsavedWord(wordId: Int, wordlistId: Int) = runBlocking {
        val crossRef = SavedWordCrossRef(wordId = wordId, wordlistId = wordlistId)

        wordlistDao.unsaveWord(crossRef)
    }
}

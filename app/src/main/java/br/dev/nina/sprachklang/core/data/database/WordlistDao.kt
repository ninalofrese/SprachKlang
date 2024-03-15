package br.dev.nina.sprachklang.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import br.dev.nina.sprachklang.core.data.database.entities.EntryResult
import br.dev.nina.sprachklang.core.data.database.entities.SavedWordCrossRef
import br.dev.nina.sprachklang.core.data.database.entities.WordlistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordlistDao {

    @Upsert
    suspend fun insertWordlist(wordlistEntity: WordlistEntity): Long

    @Query(
        value = """
            DELETE FROM Wordlists
            WHERE list_id == (:collectionId)
        """,
    )
    suspend fun deleteWordlist(collectionId: Int)

    @Transaction
    @Query("""
        SELECT Wordlists.* FROM Wordlists
        WHERE Wordlists.list_id = :wordlistId
    """)
    fun getWordlistById(wordlistId: Int): WordlistEntity

    @Query(
        value = """
            SELECT *
            FROM Wordlists
        """,
    )
    fun getCollections(): Flow<List<WordlistEntity>>

    @Upsert
    suspend fun saveWord(savedWordCrossRef: SavedWordCrossRef)

    @Delete
    suspend fun unsaveWord(savedWordCrossRef: SavedWordCrossRef)

    @Transaction
    @Query("""
        SELECT * FROM SavedWords  
        INNER JOIN Wordlists ON Wordlists.list_id = SavedWords.collection_id
        WHERE SavedWords.word_id = :wordId
    """)
    fun getWordlistsForWord(wordId: Int): Flow<List<WordlistEntity>>

    @Transaction
    @Query(
        value = """
            SELECT *
            FROM SavedWords
            JOIN Headwords ON SavedWords.word_id = Headwords.id
            JOIN Definitions ON Headwords.id = Definitions.word_id
            JOIN Wordlists ON SavedWords.collection_id = Wordlists.list_id
            WHERE SavedWords.collection_id == :wordlistId
        """,
    )
    fun getWordsByListId(wordlistId: Int?): Flow<List<EntryResult>>

    @Transaction
    suspend fun createWordlistWithWord(wordId: Int, wordlist: WordlistEntity) {
        val wordlistId = insertWordlist(wordlist)
        saveWord(SavedWordCrossRef(wordId = wordId, wordlistId = wordlistId.toInt()))
    }
}

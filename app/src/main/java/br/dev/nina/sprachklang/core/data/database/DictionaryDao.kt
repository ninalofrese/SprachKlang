package br.dev.nina.sprachklang.core.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import br.dev.nina.sprachklang.core.data.database.entities.HeadwordEntity
import br.dev.nina.sprachklang.core.data.database.entities.EntryResult

@Dao
interface DictionaryDao {
    @Query("""
        SELECT * FROM Headwords
        WHERE LOWER(word) LIKE '%' || LOWER(:query) || '%'
        ORDER BY 
            CASE
                WHEN LOWER(word) = LOWER(:query) THEN 1
                WHEN LOWER(word) LIKE LOWER(:query) || '%' THEN 2
                ELSE 3
            END,
            word ASC
        LIMIT :limit OFFSET :offset
    """)
    fun searchWord(query: String, limit: Int, offset: Int): List<HeadwordEntity>

    @Transaction
    @Query("""
        SELECT * FROM Headwords
        WHERE Headwords.id == :wordId
    """)
    fun getWordDetails(wordId: Int): List<EntryResult>
}

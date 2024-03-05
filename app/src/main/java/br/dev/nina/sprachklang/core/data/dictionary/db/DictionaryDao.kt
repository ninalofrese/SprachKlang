package br.dev.nina.sprachklang.core.data.dictionary.db

import androidx.room.Dao
import androidx.room.Query
import br.dev.nina.sprachklang.core.data.db.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.db.entities.HeadwordEntity

@Dao
interface DictionaryDao {

    @Query("""
        SELECT * FROM Headwords
        LEFT JOIN Definitions ON headwords.id = definitions.word_id
        WHERE LOWER(word) LIKE '%' || LOWER(:query) || '%'
        ORDER BY word ASC
        LIMIT :limit OFFSET :offset
    """)
    fun searchWordWithDefinition(query: String, limit: Int, offset: Int): Map<HeadwordEntity, List<DefinitionEntity>>

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

    @Query("""
        SELECT * FROM Headwords
        LEFT JOIN Definitions ON headwords.id = definitions.word_id
        WHERE id == :wordId
        LIMIT 1
    """)
    fun getWordDetails(wordId: Int): Map<HeadwordEntity, List<DefinitionEntity>>
}

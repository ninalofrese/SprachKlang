package br.dev.nina.sprachklang.dictionarysearch.data.source.db

import androidx.room.Dao
import androidx.room.Query
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities.DefinitionEntity
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities.EntryEntity

@Dao
interface DictionaryDao {

    @Query("""
        SELECT * FROM Entries
        LEFT JOIN Definitions ON entries.entry_id = definitions.word_id
        WHERE LOWER(word) LIKE '%' || LOWER(:query) || '%'
        LIMIT :limit OFFSET :offset
    """)
    fun searchWordWithDefinition(query: String, limit: Int, offset: Int): Map<EntryEntity, List<DefinitionEntity>>

    @Query("""
        SELECT * FROM Entries
        WHERE LOWER(word) LIKE '%' || LOWER(:query) || '%'
        LIMIT :limit OFFSET :offset
    """)
    fun searchWord(query: String, limit: Int, offset: Int): List<EntryEntity>

    @Query("""
        SELECT * FROM Entries
        LEFT JOIN Definitions ON entries.entry_id = definitions.word_id
        WHERE entry_id == :wordId
    """)
    fun getWordDetails(wordId: Int): Map<EntryEntity, List<DefinitionEntity>>
}

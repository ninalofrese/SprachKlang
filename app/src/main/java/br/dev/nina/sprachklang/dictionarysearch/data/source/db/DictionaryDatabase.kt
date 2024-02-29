package br.dev.nina.sprachklang.dictionarysearch.data.source.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities.DefinitionEntity
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities.DictionaryConverters
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.entities.EntryEntity

@Database(
    entities = [EntryEntity::class, DefinitionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DictionaryConverters::class)
abstract class DictionaryDatabase: RoomDatabase() {
    abstract val dao: DictionaryDao
}

package br.dev.nina.sprachklang.core.data.dictionary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.nina.sprachklang.core.data.db.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.db.entities.DictionaryConverters
import br.dev.nina.sprachklang.core.data.db.entities.HeadwordEntity

@Database(
    entities = [HeadwordEntity::class, DefinitionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DictionaryConverters::class)
abstract class DictionaryDatabase: RoomDatabase() {
    abstract val dao: DictionaryDao
}

package br.dev.nina.sprachklang.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.dev.nina.sprachklang.core.data.database.entities.DefinitionEntity
import br.dev.nina.sprachklang.core.data.database.entities.DictionaryConverters
import br.dev.nina.sprachklang.core.data.database.entities.HeadwordEntity
import br.dev.nina.sprachklang.core.data.database.entities.SavedWordCrossRef
import br.dev.nina.sprachklang.core.data.database.entities.WordlistEntity

@Database(
    entities = [
        HeadwordEntity::class,
        DefinitionEntity::class,
        WordlistEntity::class,
        SavedWordCrossRef::class,
   ],
    version = 1,
    exportSchema = true
)
@TypeConverters(DictionaryConverters::class)
abstract class DictionaryDatabase: RoomDatabase() {
    abstract val dictionaryDao: DictionaryDao
    abstract val wordlistDao: WordlistDao
}

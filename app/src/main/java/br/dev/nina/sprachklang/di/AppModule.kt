package br.dev.nina.sprachklang.di

import android.content.Context
import androidx.room.Room
import br.dev.nina.sprachklang.core.data.DictionaryRepositoryImpl
import br.dev.nina.sprachklang.core.data.WordlistRepositoryImpl
import br.dev.nina.sprachklang.core.data.database.DictionaryDatabase
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.util.DispatcherProvider
import br.dev.nina.sprachklang.core.util.Dispatchers
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDictionaryDatabase(@ApplicationContext appContext: Context): DictionaryDatabase {
        return Room.databaseBuilder(appContext, DictionaryDatabase::class.java, "dictionaryDb")
            .createFromAsset("databases/GermanDictionary.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Singleton
    @Provides
    fun providesDispatcher(): DispatcherProvider {
        return Dispatchers()
    }

    @Provides
    @Singleton
    fun provideDictionaryRepository(db: DictionaryDatabase, dispatcher: Dispatchers): DictionaryRepository {
        return DictionaryRepositoryImpl(db.dictionaryDao, dispatcher)
    }

    @Provides
    @Singleton
    fun provideWordlistRepository(db: DictionaryDatabase, dispatcher: Dispatchers): WordlistRepository {
        return WordlistRepositoryImpl(db.wordlistDao, dispatcher)
    }
}

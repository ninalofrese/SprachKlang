package br.dev.nina.sprachklang.di

import android.content.Context
import androidx.room.Room
import br.dev.nina.sprachklang.dictionarysearch.data.source.db.DictionaryDatabase
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
            .createFromAsset("databases/GermanAusspracheDictionary.db")
            .build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }
}

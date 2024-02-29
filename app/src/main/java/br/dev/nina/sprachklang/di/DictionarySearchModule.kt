package br.dev.nina.sprachklang.di

import br.dev.nina.sprachklang.dictionarysearch.data.DictionaryRepositoryImpl
import br.dev.nina.sprachklang.dictionarysearch.domain.DictionaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DictionarySearchModule {

    @Binds
    @Singleton
    abstract fun bindsDictionaryRepository(
        dictionaryRepository: DictionaryRepositoryImpl
    ): DictionaryRepository

}

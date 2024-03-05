package br.dev.nina.sprachklang.di

import br.dev.nina.sprachklang.core.data.dictionary.DictionaryRepositoryImpl
import br.dev.nina.sprachklang.core.domain.dictionary.DictionaryRepository
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

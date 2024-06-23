package br.dev.nina.sprachklang.feature_word.presentation

import androidx.lifecycle.SavedStateHandle
import br.dev.nina.sprachklang.core.MainDispatcherRule
import br.dev.nina.sprachklang.core.data.testdoubles.FakeDictionaryRepository
import br.dev.nina.sprachklang.core.data.testdoubles.FakeWordlistRepository
import br.dev.nina.sprachklang.core.data.testdoubles.entriesTestData
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.core.presentation.components.saveword.SaveWordModalEvent
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WordViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var dictionaryRepository: FakeDictionaryRepository
    private lateinit var wordlistRepository: FakeWordlistRepository
    private lateinit var viewModel: WordViewModel
    private val savedStateHandle = SavedStateHandle(mapOf("entryId" to 1))

    private val expectedWordlist = Wordlist(id = 1, name = "Test List")
    private val expectedWord = entriesTestData.first()

    @Before
    fun setUp() {
        dictionaryRepository = FakeDictionaryRepository()
        wordlistRepository = FakeWordlistRepository()

        runBlocking {
            wordlistRepository.createWordlist(expectedWordlist)
            wordlistRepository.saveWord(1, 1)
        }

        viewModel = WordViewModel(savedStateHandle, dictionaryRepository, wordlistRepository)
    }

    @Test
    fun `when adding a list with empty name, should not create wordlist`() = runTest {
        viewModel.onDialogEvent(NewListDialogEvent.SetListName(""))
        viewModel.onDialogEvent(NewListDialogEvent.AddList)

        assertThat(wordlistRepository.createWordlistAndSaveWordCalled).isFalse()
    }

    @Test
    fun `when adding a valid list, should create wordlist`() = runTest {
        viewModel.onDialogEvent(NewListDialogEvent.SetListName("New List"))
        viewModel.onDialogEvent(NewListDialogEvent.AddList)

        assertThat(wordlistRepository.createWordlistAndSaveWordCalled).isTrue()
    }

    @Test
    fun `when saving a word to a list, should call repository`() = runTest {
        val wordlist = Wordlist(id = 2, name = "Another List")
        wordlistRepository.createWordlist(wordlist)
        viewModel.onModalEvent(SaveWordModalEvent.ToggleSelectedWordlist(wordlist))

        assertThat(wordlistRepository.saveWordCalled).isTrue()
    }

    @Test
    fun `when unsaving a word from a list, should call repository`() = runTest {
        val wordlist = Wordlist(id = 1, name = "Test List")
        wordlistRepository.createWordlist(wordlist)
        viewModel.onModalEvent(SaveWordModalEvent.ToggleSelectedWordlist(wordlist))
        viewModel.onModalEvent(SaveWordModalEvent.ToggleSelectedWordlist(wordlist))

        assertThat(wordlistRepository.unsaveWordCalled).isTrue()
    }
}
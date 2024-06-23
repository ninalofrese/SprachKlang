package br.dev.nina.sprachklang.feature_wordlist


import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import br.dev.nina.sprachklang.core.MainDispatcherRule
import br.dev.nina.sprachklang.core.data.testdoubles.FakeWordlistRepository
import br.dev.nina.sprachklang.core.data.testdoubles.entriesTestData
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WordlistViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: WordlistRepository = FakeWordlistRepository()
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: WordlistViewModel

    private val expectedWordlist = Wordlist(id = 1, name = "Test List")
    private val expectedWord = entriesTestData.first()

    @Before
    fun setUp() {
        savedStateHandle = SavedStateHandle(mapOf("wordlistId" to 1))

        runBlocking {
            (repository as FakeWordlistRepository).createWordlist(expectedWordlist)
            repository.saveWord(1, 1)
        }

        viewModel = WordlistViewModel(repository, savedStateHandle)
    }

    @Test
    fun `when unsaving a word, should call repository and show snackbar`() = runTest {
        (repository as FakeWordlistRepository).reset()

        viewModel.snackBarFlow.test {
            viewModel.onEvent(WordlistEvent.UnsaveWord(expectedWord.id))

            assertThat(repository.unsaveWordCalled).isTrue()
            assertThat(repository.unsaveWordId).isEqualTo(expectedWord.id)

            assertThat(awaitItem()).isEqualTo("Word ${expectedWord.word} removed from list ${expectedWordlist.name}")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when unsaving a word fails, should show error snackbar`() = runTest {
        (repository as FakeWordlistRepository).reset()
        repository.isError = true


        viewModel.snackBarFlow.test {
            viewModel.onEvent(WordlistEvent.UnsaveWord(1))

            assertThat(awaitItem()).isEqualTo("Failed to remove word ${expectedWord.word} from list ${expectedWordlist.name}")
            cancelAndIgnoreRemainingEvents()
        }
    }
}
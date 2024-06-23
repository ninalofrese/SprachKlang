package br.dev.nina.sprachklang.feature_home.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import br.dev.nina.sprachklang.core.MainDispatcherRule
import br.dev.nina.sprachklang.core.data.testdoubles.FakeWordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.WordlistRepository
import br.dev.nina.sprachklang.core.domain.dictionary.model.Wordlist
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListDialogEvent
import br.dev.nina.sprachklang.core.presentation.components.newlist.NewListState
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var repository: WordlistRepository
    private val savedStateHandle = SavedStateHandle()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        repository = FakeWordlistRepository()
        viewModel = HomeViewModel(repository, savedStateHandle)
    }

    @Test
    fun `when view model is initialized, then ui states should be initial`() = runTest {
        viewModel.newListState.test {
            assertThat(awaitItem()).isEqualTo(NewListState())
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.wordlistState.test {
            assertThat(awaitItem()).isEqualTo(HomeWordlistState(isLoading = true))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when adding a list with valid name, should create wordlist and show snackbar`() = runTest {
        (repository as FakeWordlistRepository).reset()
        val testListName = "Test List"

        turbineScope {
            val newListState = viewModel.newListState.testIn(backgroundScope)
            val snackBarFlow = viewModel.snackBarFlow.testIn(backgroundScope)

            newListState.awaitItem()

            viewModel.onDialogEvent(NewListDialogEvent.SetListName(testListName))
            assertThat(savedStateHandle.get<String>("listNameInput")).isEqualTo(testListName)
            viewModel.onDialogEvent(NewListDialogEvent.AddList)

            assertThat(newListState.awaitItem().listnameInput).isEqualTo(testListName)
            assertThat(snackBarFlow.awaitItem()).isEqualTo("Wordlist $testListName added successfully")
            newListState.cancelAndIgnoreRemainingEvents()
            snackBarFlow.cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when adding a list with empty name, should not create wordlist`() = runTest {
        (repository as FakeWordlistRepository).reset()

        viewModel.onDialogEvent(NewListDialogEvent.SetListName(""))
        viewModel.onDialogEvent(NewListDialogEvent.AddList)

        assertThat((repository as FakeWordlistRepository).createWordlistCalled).isFalse()
    }

    @Test
    fun `when deleting a wordlist, should call repository and show snackbar`() = runTest {
        (repository as FakeWordlistRepository).reset()

        val testWordlist = Wordlist(id = 1, name = "Test List")

        viewModel.snackBarFlow.test {
            viewModel.onEvent(HomeWordlistEvent.DeleteWordList(testWordlist))

            assertThat((repository as FakeWordlistRepository).deleteWordlistCalled).isTrue()

            assertThat(awaitItem()).isEqualTo("Wordlist ${testWordlist.name} removed successfully")
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `when deleting a wordlist fails, should show error snackbar`() = runTest {
        (repository as FakeWordlistRepository).reset()
        (repository as FakeWordlistRepository).isError = true

        val testWordlist = Wordlist(id = 1, name = "Test List")

        viewModel.snackBarFlow.test {
            viewModel.onEvent(HomeWordlistEvent.DeleteWordList(testWordlist))

            assertThat((repository as FakeWordlistRepository).deleteWordlistCalled).isTrue()

            assertThat(awaitItem()).isEqualTo("Failed to remove wordlist ${testWordlist.name}")
            cancelAndIgnoreRemainingEvents()
        }
    }
}

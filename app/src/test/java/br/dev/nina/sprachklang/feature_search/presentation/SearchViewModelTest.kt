package br.dev.nina.sprachklang.feature_search.presentation

import br.dev.nina.sprachklang.core.MainDispatcherRule
import br.dev.nina.sprachklang.core.TestDispatchers
import br.dev.nina.sprachklang.core.data.testdoubles.FakeDictionaryRepository
import br.dev.nina.sprachklang.core.data.testdoubles.dummyHeadword
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: FakeDictionaryRepository
    private lateinit var dispatcher: TestDispatchers

    @Before
    fun setUp() {
        dispatcher = TestDispatchers()
        repository = FakeDictionaryRepository()
        viewModel = SearchViewModel(repository, dispatcher)
    }

    @Test
    fun `initial test is set correctly`() {
        assertThat(viewModel.state.items).isEmpty()
        assertThat(viewModel.state.page).isEqualTo(0)
        assertThat(viewModel.state.isLoading).isFalse()
    }

    @Test
    fun `load next items updates state on success`() = runTest {
        val fakeHeadword = dummyHeadword(9)
        repository.addHeadwords(fakeHeadword)

        viewModel.onEvent(DictionarySearchEvent.OnSearchQueryChange("genau"))
        advanceTimeBy(500L)

        assertThat(viewModel.state.items).containsExactly(fakeHeadword)
        assertThat(viewModel.state.page).isEqualTo(1)
    }

    @Test
    fun `changing search query resets page and cancels search`() = runTest {
        val initialQuery = "genau"
        viewModel.onEvent(DictionarySearchEvent.OnSearchQueryChange(initialQuery))

        val newQuery = "wochenende"
        viewModel.onEvent(DictionarySearchEvent.OnSearchQueryChange(newQuery))

        // then
        assertThat(viewModel.state.query).isEqualTo(newQuery)
        assertThat(viewModel.state.page).isEqualTo(0)
    }
}

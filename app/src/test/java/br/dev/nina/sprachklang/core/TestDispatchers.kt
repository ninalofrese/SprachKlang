package br.dev.nina.sprachklang.core

import br.dev.nina.sprachklang.core.util.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher

class TestDispatchers: DispatcherProvider {
    private val testDispatcher = StandardTestDispatcher()
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher
    override val unconfined: CoroutineDispatcher
        get() = testDispatcher
}

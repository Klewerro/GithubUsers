package com.klewerro.githubusers.util

import com.klewerro.githubusers.core.domain.dispatcher.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestDispatchers(val testDispatcher: TestDispatcher = StandardTestDispatcher()) :
    DispatcherProvider {

    override val main: CoroutineDispatcher
        get() = testDispatcher

    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher

    override val unconfined: CoroutineDispatcher
        get() = testDispatcher
}

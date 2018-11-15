package com.tablereservation.core.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext

class TestUseCaseContextProvider : UseCaseContextProvider() {
    @ExperimentalCoroutinesApi
    override val main: CoroutineContext by lazy { Dispatchers.Unconfined }
    @ExperimentalCoroutinesApi
    override val io: CoroutineContext by lazy { Dispatchers.Unconfined }
}
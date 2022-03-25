package com.example.calorietrackerfullstack.concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


interface IAppDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
}

class AppDispatchers : IAppDispatchers {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
}
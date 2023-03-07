package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

suspend fun <T, U> callbackFlowEmit(
    callback: () -> U?,
    flow: StateFlow<T>,
    emit: suspend (callback: U) -> Unit,
) {
    val callback1 = callback()
    if (callback1 != null) {
        emit(callback1)
        return
    }

    coroutineScope {
        val collectJob = launch { flow.collect() }

        while (!collectJob.isCancelled) {
            val callback2 = callback()
            if (callback2 != null) {
                emit(callback2)
                collectJob.cancel()
            }
            delay(500)
        }
    }
}
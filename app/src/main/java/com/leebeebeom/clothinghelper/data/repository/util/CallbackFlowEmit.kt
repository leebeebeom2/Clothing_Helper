package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

suspend fun <T> callbackFlowEmit(
    callback: () -> T?,
    flow: Flow<*>,
    emit: suspend (callback: T) -> Unit,
) {
    val callback1 = callback()
    if (callback1 != null) {
        emit(callback1)
        return
    }

    coroutineScope {
        val collectJob = launch { flow.collect() }

        while (true) {
            val callback2 = callback()
            if (callback2 != null) {
                emit(callback2)
                collectJob.cancel()
                return@coroutineScope
            }
            delay(500)
        }
    }
}
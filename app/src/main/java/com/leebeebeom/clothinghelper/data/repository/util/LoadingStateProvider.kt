package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface LoadingStateProvider {
    val isLoading: SharedFlow<Boolean>
}

open class LoadingStateProviderImpl : LoadingStateProvider {

    private val _isLoading = MutableSharedFlow<Boolean>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val isLoading = _isLoading

    suspend fun loadingOn() = _isLoading.emit(true)

    suspend fun loadingOff() = _isLoading.emit(false)
}

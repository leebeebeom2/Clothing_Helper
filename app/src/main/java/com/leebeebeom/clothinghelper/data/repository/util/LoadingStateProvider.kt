package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface LoadingStateProvider {
    val isLoading: SharedFlow<Boolean>
}

open class LoadingStateProviderImpl : LoadingStateProvider {

    private val _isLoading = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val isLoading = _isLoading.asSharedFlow()

    protected suspend fun loadingOn() = _isLoading.emit(true)

    protected suspend fun loadingOff() = _isLoading.emit(false)
}

package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface LoadingStateProvider {
    val isLoading: SharedFlow<Boolean>
}

open class LoadingStateProviderImpl : LoadingStateProvider {

    private val _isLoading = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val isLoading = _isLoading

    protected fun loadingOn() = _isLoading.tryEmit(true)

    protected fun loadingOff() = _isLoading.tryEmit(false)
}

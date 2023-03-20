package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEmpty

interface LoadingStateProvider {
    val loadingStream: Flow<Boolean>
}

open class LoadingStreamProviderImpl(initialState: Boolean) : LoadingStateProvider {

    private val _loadingStream = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val loadingStream = _loadingStream.onEmpty { emit(initialState) }.distinctUntilChanged()

    protected fun loadingOn() = _loadingStream.tryEmit(true)

    protected fun loadingOff() = _loadingStream.tryEmit(false)
}

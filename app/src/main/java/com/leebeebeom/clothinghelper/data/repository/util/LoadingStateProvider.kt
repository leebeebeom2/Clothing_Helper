package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEmpty

interface LoadingStateProvider {
    val isLoading: Flow<Boolean>
}

open class LoadingStateProviderImpl(initialState: Boolean) : LoadingStateProvider {

    private val _isLoading = MutableSharedFlow<Boolean>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val isLoading = _isLoading.onEmpty { emit(initialState) }.distinctUntilChanged()

    protected suspend fun loadingOn() = _isLoading.emit(true)

    protected suspend fun loadingOff() = _isLoading.emit(false)
}

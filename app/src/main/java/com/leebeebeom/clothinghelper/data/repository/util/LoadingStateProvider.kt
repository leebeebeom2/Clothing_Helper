package com.leebeebeom.clothinghelper.data.repository.util

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

interface LoadingStateProvider {
    val isLoading: StateFlow<Boolean>
}

open class LoadingStateProviderImpl(initialLoading: Boolean) : LoadingStateProvider {
    private val _isLoading = MutableStateFlow(initialLoading)
    override val isLoading = _isLoading.asStateFlow()

    fun loadingOn() = _isLoading.update { true }

    fun loadingOff() = _isLoading.update { false }
}
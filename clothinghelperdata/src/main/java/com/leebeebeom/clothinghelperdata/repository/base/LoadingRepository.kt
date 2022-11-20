package com.leebeebeom.clothinghelperdata.repository.base

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

abstract class LoadingRepository(initialLoading: Boolean) {
    private val _isLoading = MutableStateFlow(initialLoading)
    val isLoading get() = _isLoading.asStateFlow()

    protected fun loadingOn() = _isLoading.update { true }

    protected suspend fun loadingOff() = withContext(NonCancellable) { _isLoading.update { false } }
}
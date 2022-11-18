package com.leebeebeom.clothinghelperdata.repository.base

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

abstract class BaseRepository(isLoading: Boolean) {
    private val _isLoading = MutableStateFlow(isLoading)
    val isLoading get() = _isLoading.asStateFlow()

    fun loadingOn() = _isLoading.update { true }

    suspend fun loadingOff() = withContext(NonCancellable) { _isLoading.update { false } }
}
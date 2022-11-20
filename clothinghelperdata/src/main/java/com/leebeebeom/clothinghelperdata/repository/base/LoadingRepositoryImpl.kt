package com.leebeebeom.clothinghelperdata.repository.base

import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

interface LoadingRepository {
    val isLoading: StateFlow<Boolean>
    fun loadingOn()
    suspend fun loadingOff()
}

class LoadingRepositoryImpl(initialLoading: Boolean) : LoadingRepository {
    private val _isLoading = MutableStateFlow(initialLoading)
    override val isLoading get() = _isLoading.asStateFlow()

    override fun loadingOn() = _isLoading.update { true }

    override suspend fun loadingOff() = withContext(NonCancellable) { _isLoading.update { false } }
}
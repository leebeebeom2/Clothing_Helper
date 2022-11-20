package com.leebeebeom.clothinghelperdata.repository.base

import com.leebeebeom.clothinghelperdomain.repository.LoadingRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

class LoadingRepositoryImpl(initialLoading: Boolean) : LoadingRepository {
    private val _isLoading = MutableStateFlow(initialLoading)
    override val isLoading get() = _isLoading.asStateFlow()

    fun loadingOn() = _isLoading.update { true }

    suspend fun loadingOff() = withContext(NonCancellable) { _isLoading.update { false } }
}
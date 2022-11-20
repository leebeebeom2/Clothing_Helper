package com.leebeebeom.clothinghelperdata.repository.base

import com.leebeebeom.clothinghelperdomain.repository.LoadingRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext

/**
 * [loadingOn]과 [loadingOff]로 [isLoading]의 상태 변경
 */
class LoadingRepositoryImpl(initialLoading: Boolean) : LoadingRepository {
    private val _isLoading = MutableStateFlow(initialLoading)
    override val isLoading get() = _isLoading.asStateFlow()

    fun loadingOn() = _isLoading.update { true }

    /**
     * [NonCancellable]로 코루틴이 취소되어도 동작 보장
     */
    suspend fun loadingOff() = withContext(NonCancellable) { _isLoading.update { false } }
}
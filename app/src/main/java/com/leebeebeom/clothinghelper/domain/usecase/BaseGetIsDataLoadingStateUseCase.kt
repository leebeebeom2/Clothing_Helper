package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseGetIsDataLoadingStateUseCase(
    private val repository: BaseDataRepository<*>,
    private val appScope: CoroutineScope,
) {
    private lateinit var isLoading: StateFlow<Boolean>
    fun getLoadingFlow(): StateFlow<Boolean> {
        if (::isLoading.isInitialized) return isLoading

        isLoading = repository.isLoading.stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
        return isLoading
    }
}
package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

abstract class BaseGetIsDataLoadingStateUseCase(
    private val repository: LoadingStateProvider,
    private val appScope: CoroutineScope,
) {
    private lateinit var isLoading: StateFlow<Boolean>
    fun getLoadingFlow(initialValue: Boolean): StateFlow<Boolean> {
        if (::isLoading.isInitialized) return isLoading

        isLoading = repository.isLoading.stateIn(
            scope = appScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = initialValue
        )
        return isLoading
    }
}
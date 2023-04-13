package com.leebeebeom.clothinghelper.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable

@OptIn(SavedStateHandleSaveableApi::class)
abstract class LoadingViewModel(
    savedToastTextsKey: String,
    initialLoading: Boolean,
    savedLoadingKey: String,
    savedStateHandle: SavedStateHandle,
) : ToastViewModel(savedToastTextsKey = savedToastTextsKey, savedStateHandle = savedStateHandle) {
    private var isLoadingState by savedStateHandle.saveable(key = savedLoadingKey) {
        mutableStateOf(initialLoading)
    }
    protected val isLoadingFlow = snapshotFlow { isLoadingState }
    protected fun setLoading(loading: Boolean) {
        isLoadingState = loading
    }
}
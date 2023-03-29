package com.leebeebeom.clothinghelper.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

abstract class LoadingViewModel(initialLoading: Boolean) : ToastViewModel() {
    protected var isLoadingState by mutableStateOf(initialLoading)
    protected val isLoadingFlow = snapshotFlow { isLoadingState }
}
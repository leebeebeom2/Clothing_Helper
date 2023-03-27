package com.leebeebeom.clothinghelper.ui.signin.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

abstract class LoadingState {
    private var isLoadingState by mutableStateOf(false)
    protected val isLoadingFlow = snapshotFlow { isLoadingState }

    fun setLoading(loading: Boolean) {
        isLoadingState = loading
    }
}
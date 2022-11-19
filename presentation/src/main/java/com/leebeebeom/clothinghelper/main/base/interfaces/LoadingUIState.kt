package com.leebeebeom.clothinghelper.main.base.interfaces

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface LoadingUIState {
    val isLoading: Boolean
    fun updateIsLoading(isLoading: Boolean)
}

class LoadingUIStateImpl : LoadingUIState {
    override var isLoading by mutableStateOf(false)
        private set

    override fun updateIsLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }
}


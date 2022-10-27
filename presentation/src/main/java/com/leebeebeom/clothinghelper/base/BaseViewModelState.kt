package com.leebeebeom.clothinghelper.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class BaseViewModelState {
    var isLoading: Boolean by mutableStateOf(false)
        private set
    var toastText: Int? by mutableStateOf(null)
        private set

    val toastShown = { toastText = null }

    val showToast = { toastText: Int -> this.toastText = toastText }

    val loadingOff = { isLoading = false }

    val loadingOn = { isLoading = true }
}
package com.leebeebeom.clothinghelper.ui.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class BaseViewModelState {
    var isLoading: Boolean by mutableStateOf(false)
        private set
    var toastText: Int? by mutableStateOf(null)
        private set

    val toastShown = { toastText = null }

    fun showToast(@StringRes toastText: Int) {
        this.toastText = toastText
    }

    fun loadingOff() {
        isLoading = false
    }

    fun loadingOn() {
        isLoading = true
    }
}
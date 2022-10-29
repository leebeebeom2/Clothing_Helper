package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class BaseViewModelState {
    var isLoading: Boolean by mutableStateOf(false)
        private set
    var toastText: Int? by mutableStateOf(null)
        private set

    fun toastShown() {
        toastText = null
    }

    fun showToast(@StringRes text: Int) {
        toastText = text
    }

    fun loadingOn() {
        isLoading = true
    }

    fun loadingOff() {
        isLoading = false
    }
}
package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class BaseUIState {
    var toastText: Int? by mutableStateOf(null)
        private set

    fun showToast(@StringRes text: Int?) {
        toastText = text
    }

    fun toastShown() {
        toastText = null
    }
}
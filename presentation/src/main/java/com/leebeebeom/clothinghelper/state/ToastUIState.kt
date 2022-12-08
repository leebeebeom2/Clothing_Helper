package com.leebeebeom.clothinghelper.state

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface ToastUIState {
    val toastText: Int?

    fun showToast(@StringRes text: Int?)
    fun toastShown()
}

class ToastUIStateImpl : ToastUIState {
    override var toastText: Int? by mutableStateOf(null)
        private set

    override fun showToast(@StringRes text: Int?) {
        toastText = text
    }

    override fun toastShown() {
        toastText = null
    }
}
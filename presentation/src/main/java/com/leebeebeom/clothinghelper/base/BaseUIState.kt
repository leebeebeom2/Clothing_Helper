package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

open class BaseUIState {
    @StringRes
    private val _toastText: MutableState<Int?> = mutableStateOf(null)

    val toastText by derivedStateOf { _toastText.value }

    fun showToast(@StringRes text: Int?) {
        _toastText.value = text
    }

    fun toastShown() {
        _toastText.value = null
    }
}
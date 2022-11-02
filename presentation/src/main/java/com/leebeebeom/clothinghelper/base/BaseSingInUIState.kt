package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    abstract fun showToast(@StringRes toastText: Int?)
    abstract fun toastShown()
}

abstract class BaseUIState {
    abstract val toastText: Int?
}
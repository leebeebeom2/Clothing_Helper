package com.leebeebeom.clothinghelper.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel

abstract class BaseSignInViewModel : ViewModel() {
    abstract fun showToast(@StringRes toastText: Int?)
    abstract fun toastShown()
    abstract fun updateEmailError(@StringRes error: Int?)
}

abstract class BaseUIState {
    abstract val toastText: Int?
}
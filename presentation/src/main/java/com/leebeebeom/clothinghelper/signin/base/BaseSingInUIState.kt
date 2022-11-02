package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.base.BaseUIState

abstract class BaseSignInViewModel : ViewModel() {
    abstract fun showToast(@StringRes toastText: Int?)
    abstract fun toastShown()
    abstract fun updateEmailError(@StringRes error: Int?)
}

abstract class BaseSignInUIState : BaseUIState() {
    abstract val isNotError: Boolean
}
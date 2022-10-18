package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.ui.base.BaseViewModelState

open class BaseSignInViewModelState : BaseViewModelState() {
    var emailError: Int? by mutableStateOf(null)
        private set

    fun emailErrorOn(@StringRes error: Int) {
        emailError = error
    }

    fun emailErrorOff() {
        emailError = null
    }
}

open class BaseSignInUpViewModelState : BaseSignInViewModelState() {
    var googleButtonEnabled by mutableStateOf(true)
        private set

    fun googleButtonEnable() {
        googleButtonEnabled = true
    }

    fun googleButtonDisable() {
        googleButtonEnabled = false
    }
}
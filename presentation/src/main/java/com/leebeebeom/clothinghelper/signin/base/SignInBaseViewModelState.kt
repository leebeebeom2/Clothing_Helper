package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseViewModelState

open class EmailViewModelState : BaseViewModelState() {
    var emailError: Int? by mutableStateOf(null)
        private set

    fun updateEmailError(@StringRes error: Int?) {
        emailError = error
    }
}

open class GoogleSignInViewModelState : EmailViewModelState() {
    var googleButtonEnabled by mutableStateOf(true)
        private set

    fun updateGoogleButtonEnabled(enabled: Boolean) {
        googleButtonEnabled = enabled
    }
}
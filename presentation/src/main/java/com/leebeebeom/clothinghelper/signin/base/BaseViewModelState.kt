package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseViewModelState

open class BaseSignInViewModelState : BaseViewModelState() {
    var emailError: Int? by mutableStateOf(null)
        private set

    fun showEmailError(@StringRes error: Int) {
        emailError = error
    }

    fun hideEmailError() {
        emailError = null
    }
}

open class GoogleSignInViewModelState : BaseSignInViewModelState() {
    var googleButtonEnabled by mutableStateOf(true)
        private set

    fun setGoogleButtonEnable() {
        googleButtonEnabled = true
    }

    fun setGoogleButtonDisable() {
        googleButtonEnabled = false
    }
}
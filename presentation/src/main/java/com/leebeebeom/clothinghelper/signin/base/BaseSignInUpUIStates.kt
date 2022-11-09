package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

open class BaseSignInUpUIStates {
    @StringRes
    private val _toastText: MutableState<Int?> = mutableStateOf(null)

    @StringRes
    private val _emailError: MutableState<Int?> = mutableStateOf(null)

    @StringRes
    private val _passwordError: MutableState<Int?> = mutableStateOf(null)

    private val _googleButtonEnabled: MutableState<Boolean> = mutableStateOf(false)

    val toastText by derivedStateOf { _toastText.value }
    val googleButtonEnabled by derivedStateOf { _googleButtonEnabled.value }
    val emailError by derivedStateOf { _emailError.value }
    val passwordError by derivedStateOf { _passwordError.value }

    var email = ""
        private set
    var password = ""
        private set

    fun updateEmailError(@StringRes error: Int?) {
        _emailError.value = error
    }

    fun updatePasswordError(@StringRes error: Int?) {
        _passwordError.value = error
    }

    fun updateGoogleButtonEnabled(enabled: Boolean) {
        _googleButtonEnabled.value = enabled
    }

    fun showToast(@StringRes text: Int?) {
        _toastText.value = text
    }

    fun toastShown() {
        _toastText.value = null
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    open fun onPasswordChange(password: String) {
        this.password = password
    }

    open val buttonEnabled by derivedStateOf {
        emailError == null && passwordError == null && email.isNotBlank() && password.isNotBlank()
    }
}
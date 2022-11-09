package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.leebeebeom.clothinghelper.base.BaseUIState

open class BaseEmailUIStates : BaseUIState() {

    @StringRes
    private val _emailError: MutableState<Int?> = mutableStateOf(null)

    val emailError by derivedStateOf { _emailError.value }

    var email = ""
        private set

    fun updateEmailError(@StringRes error: Int?) {
        _emailError.value = error
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    open val buttonEnabled by derivedStateOf { email.isNotBlank() && emailError == null }
}

open class BaseSignInUpUIStates : BaseEmailUIStates() {
    @StringRes
    private val _passwordError: MutableState<Int?> = mutableStateOf(null)

    private val _googleButtonEnabled: MutableState<Boolean> = mutableStateOf(false)


    val googleButtonEnabled by derivedStateOf { _googleButtonEnabled.value }
    val passwordError by derivedStateOf { _passwordError.value }

    var password = ""
        private set

    fun updatePasswordError(@StringRes error: Int?) {
        _passwordError.value = error
    }

    fun updateGoogleButtonEnabled(enabled: Boolean) {
        _googleButtonEnabled.value = enabled
    }

    open fun onPasswordChange(password: String) {
        this.password = password
    }

    override val buttonEnabled by derivedStateOf {
        super.buttonEnabled && passwordError == null && password.isNotBlank()
    }
}
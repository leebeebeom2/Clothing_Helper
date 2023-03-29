package com.leebeebeom.clothinghelper.ui.signin.state

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class PasswordState(
    initialEmail: String,
    initialEmailError: Int?,
    initialPassword: String,
    initialPasswordError: Int?,
) : EmailState(initialEmail = initialEmail, initialEmailError = initialEmailError) {
    var password by mutableStateOf(initialPassword)
        private set
    var passwordError: Int? by mutableStateOf(initialPasswordError)
        protected set
    override val buttonEnabled by derivedStateOf { super.buttonEnabled && password.isNotBlank() && passwordError == null }

    open fun onPasswordChange(password: String) {
        if (this.password == password) return
        this.password = password
        passwordError = null
    }

    fun setPasswordError(@StringRes error: Int) {
        passwordError = error
    }
}
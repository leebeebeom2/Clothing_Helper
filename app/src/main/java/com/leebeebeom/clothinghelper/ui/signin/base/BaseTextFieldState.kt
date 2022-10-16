package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class EmailUIState(
    initialEmail: String = "",
    @StringRes initialEmailError: Int? = null
) {
    var email: String by mutableStateOf(initialEmail)
    var emailError: Int? by mutableStateOf(initialEmailError)

    fun onEmailChange(email: String) {
        this.email = email
        emailError = null
    }

    fun emailErrorEnabled(error: Int) {
        emailError = error
    }
}

open class PasswordUIState(
    initialEmail: String = "",
    @StringRes initialEmailError: Int? = null,
    initialPassword: String = "",
    @StringRes initialPasswordError: Int? = null
) : EmailUIState(initialEmail, initialEmailError) {
    var password: String by mutableStateOf(initialPassword)
    var passwordError: Int? by mutableStateOf(initialPasswordError)

    open fun onPasswordChange(password: String) {
        this.password = password
        passwordError = null
    }

    fun passwordErrorEnabled(error: Int) {
        passwordError = error
    }
}
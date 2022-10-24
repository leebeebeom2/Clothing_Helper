package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class EmailUIState(email: String = "") {
    var email: String by mutableStateOf(email)

    fun onEmailChange(email: String, hideEmailError: () -> Unit) {
        this.email = email
        hideEmailError()
    }
}

open class PasswordUIState(email: String = "", password: String = "") :
    EmailUIState(email) {
    var password: String by mutableStateOf(password)

    open fun onPasswordChange(password: String, hidePasswordError: () -> Unit) {
        this.password = password
        hidePasswordError()
    }
}
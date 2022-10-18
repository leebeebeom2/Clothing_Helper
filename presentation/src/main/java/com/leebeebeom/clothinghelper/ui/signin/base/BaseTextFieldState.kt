package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class EmailUIState(initialEmail: String = "") {
    var email: String by mutableStateOf(initialEmail)

    fun onEmailChange(email: String, errorOff: () -> Unit) {
        this.email = email
        errorOff()
    }
}

open class PasswordUIState(initialEmail: String = "", initialPassword: String = "") :
    EmailUIState(initialEmail) {
    var password: String by mutableStateOf(initialPassword)

    open fun onPasswordChange(password: String, errorOff: () -> Unit) {
        this.password = password
        errorOff()
    }
}
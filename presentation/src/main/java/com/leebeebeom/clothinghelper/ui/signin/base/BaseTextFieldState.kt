package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class EmailUIState(email: String = "") {
    var email: String by mutableStateOf(email)

    fun onEmailChange(email: String, errorOff: () -> Unit) {
        this.email = email
        errorOff()
    }
}

open class PasswordUIState(email: String = "", password: String = "") :
    EmailUIState(email) {
    var password: String by mutableStateOf(password)

    open fun onPasswordChange(password: String, errorOff: () -> Unit) {
        this.password = password
        errorOff()
    }
}
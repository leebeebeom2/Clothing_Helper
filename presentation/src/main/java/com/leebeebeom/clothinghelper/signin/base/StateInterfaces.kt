package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.MutableState

interface EmailState {
    val emailState: MutableState<String>

    fun onEmailChange(email: String) {
        emailState.value = email.trim()
    }
}

interface PasswordState {
    val passwordState: MutableState<String>

    fun onPasswordChange(password: String) {
        passwordState.value = password.trim()
    }
}
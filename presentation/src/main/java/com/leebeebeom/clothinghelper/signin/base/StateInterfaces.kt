package com.leebeebeom.clothinghelper.signin.base

interface EmailState {
    var email: String

    fun onEmailChange(email: String) {
        this.email = email.trim()
    }
}

interface PasswordState {
    var password: String

    fun onPasswordChange(password: String) {
        this.password = password.trim()
    }
}
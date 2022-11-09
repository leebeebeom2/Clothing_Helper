package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import com.leebeebeom.clothinghelper.base.BaseUIState

open class BaseEmailUIStates : BaseUIState() {
    var emailError: Int? by mutableStateOf(null)
        private set

    var email = ""
        private set

    fun updateEmailError(@StringRes error: Int?) {
        emailError = error
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    open val buttonEnabled by derivedStateOf { email.isNotBlank() && emailError == null }
}

open class BaseSignInUpUIStates : BaseEmailUIStates() {
    var passwordError: Int? by mutableStateOf(null)
        private set

    var googleButtonEnabled by mutableStateOf(false)
        private set

    var password = ""
        private set

    fun updatePasswordError(@StringRes error: Int?) {
        passwordError = error
    }

    fun updateGoogleButtonEnabled(enabled: Boolean) {
        googleButtonEnabled = enabled
    }

    open fun onPasswordChange(password: String) {
        this.password = password
    }

    override val buttonEnabled by derivedStateOf {
        super.buttonEnabled && this.passwordError == null && password.isNotBlank()
    }
}
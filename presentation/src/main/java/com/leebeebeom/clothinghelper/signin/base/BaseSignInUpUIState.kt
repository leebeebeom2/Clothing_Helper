package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseUIState

open class BaseEmailUIState : BaseUIState() {
    var emailError: Int? by mutableStateOf(null)
        private set

    var email by mutableStateOf("")
        private set

    fun updateEmailError(@StringRes error: Int?) {
        emailError = error
    }

    fun onEmailChange(email: String) {
        this.email = email
    }

    open val buttonEnabled by derivedStateOf { email.isNotBlank() && emailError == null }
}

open class BaseSignInUpUIState : BaseEmailUIState() {
    var passwordError: Int? by mutableStateOf(null)
        private set

    var googleButtonEnabled by mutableStateOf(true)
        private set

    var password by mutableStateOf("")
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
        super.buttonEnabled && passwordError == null && password.isNotBlank()
    }
}
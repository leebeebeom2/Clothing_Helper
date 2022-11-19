package com.leebeebeom.clothinghelper.signin.base.interfaces

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface PasswordUIState {
    val passwordError: Int?
    val googleButtonEnabled: Boolean
    val password: String
    fun updatePasswordError(@StringRes error: Int?)
    fun updateGoogleButtonEnabled(enabled: Boolean)
    fun onPasswordChange(password: String)
}

class PasswordUIStateImpl(baseEmailUIStateImpl: EmailUIStateImpl = EmailUIStateImpl()) :
    PasswordUIState {

    override var passwordError: Int? by mutableStateOf(null)
        private set

    override var googleButtonEnabled by mutableStateOf(true)
        private set

    override var password by mutableStateOf("")
        private set

    override fun updatePasswordError(@StringRes error: Int?) {
        passwordError = error
    }

    override fun updateGoogleButtonEnabled(enabled: Boolean) {
        googleButtonEnabled = enabled
    }

    override fun onPasswordChange(password: String) {
        this.password = password
    }

    val buttonEnabled by derivedStateOf {
        baseEmailUIStateImpl.buttonEnabled && passwordError == null && password.isNotBlank()
    }
}
package com.leebeebeom.clothinghelper.signin.base.interfaces

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface PasswordUIState : EmailUIState {
    val passwordError: Int?
    val password: String
    fun updatePasswordError(@StringRes error: Int?)
    fun onPasswordChange(password: String)
}

class PasswordUIStateImpl(emailUIStateImpl: EmailUIStateImpl = EmailUIStateImpl()) :
    EmailUIState by emailUIStateImpl, PasswordUIState {

    override var passwordError: Int? by mutableStateOf(null)
        private set

    override var password by mutableStateOf("")
        private set

    override fun updatePasswordError(@StringRes error: Int?) {
        passwordError = error
    }

    override fun onPasswordChange(password: String) {
        this.password = password
    }

    override val buttonEnabled by derivedStateOf {
        emailUIStateImpl.buttonEnabled && passwordError == null && password.isNotBlank()
    }
}
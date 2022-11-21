package com.leebeebeom.clothinghelper.signin.base.interfaces

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.ToastUIState
import com.leebeebeom.clothinghelper.base.ToastUIStateImpl

interface EmailUIState : ToastUIState {
    val emailError: Int?
    val email: String
    fun updateEmailError(@StringRes error: Int?)
    fun onEmailChange(email: String)

    val buttonEnabled: Boolean
}

class EmailUIStateImpl : EmailUIState, ToastUIState by ToastUIStateImpl() {
    override var emailError: Int? by mutableStateOf(null)
        private set

    override var email by mutableStateOf("")
        private set

    override fun updateEmailError(@StringRes error: Int?) {
        emailError = error
    }

    override fun onEmailChange(email: String) {
        this.email = email
    }

    override val buttonEnabled by derivedStateOf { email.isNotBlank() && emailError == null }
}
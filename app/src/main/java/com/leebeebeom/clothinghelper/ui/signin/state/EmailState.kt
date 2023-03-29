package com.leebeebeom.clothinghelper.ui.signin.state

import androidx.annotation.StringRes
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class EmailState(
    initialEmail: String, initialEmailError: Int?,
) {
    var email by mutableStateOf(initialEmail)
        private set
    var emailError: Int? by mutableStateOf(initialEmailError)
        private set
    open val buttonEnabled by derivedStateOf { email.isNotBlank() && emailError == null }

    fun onEmailChange(email: String) {
        if (this.email == email) return
        this.email = email
        emailError = null
    }

    fun setEmailError(@StringRes error: Int) {
        emailError = error
    }
}
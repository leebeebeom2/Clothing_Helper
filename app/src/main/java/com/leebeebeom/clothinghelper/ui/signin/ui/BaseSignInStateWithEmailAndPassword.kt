package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle

abstract class BaseSignInStateWithEmailAndPassword(
    savedStateHandle: SavedStateHandle,
    savedEmailKey: String,
    emailErrorKey: String,
    savedPasswordKey: String,
    private val passwordErrorKey: String
) : BaseSignInStateWithEmail(
    savedStateHandle = savedStateHandle,
    savedEmailKey = savedEmailKey,
    emailErrorKey = emailErrorKey
) {
    val password = SavedStateProvider(
        savedStateHandle = savedStateHandle,
        key = savedPasswordKey,
        initialValue = ""
    )

    val passwordError = SavedStateProvider<Int?>(
        savedStateHandle = savedStateHandle,
        key = savedPasswordKey,
        initialValue = null
    )

    override val buttonEnabledState by derivedStateOf { super.buttonEnabledState && password.state.isNotBlank() && passwordError.state == null }

    open fun setPassword(password: String) {
        if (this.password.state == password) return
        this.password.set(password)
        passwordError.set(null)
    }
}
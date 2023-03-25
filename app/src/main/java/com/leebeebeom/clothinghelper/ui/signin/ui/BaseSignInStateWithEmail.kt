package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle

abstract class BaseSignInStateWithEmail(
    private val savedStateHandle: SavedStateHandle,
    savedEmailKey: String,
    private val emailErrorKey: String
) {
    var savedEmail by SavedStateHandleDelegator(
        savedStateHandle = savedStateHandle,
        key = savedEmailKey,
        initial = ""
    )
        private set
    private var emailState by mutableStateOf(savedEmail)
    private var emailErrorState by mutableStateOf(savedStateHandle.get<Int>(emailErrorKey))

    protected open val buttonEnabledState by derivedStateOf { emailState.isNotBlank() && emailErrorState == null }

    fun setEmail(email: String) {
        if (emailState == email) return
        savedEmail = email
        emailState = email
        setEmailError(null)
    }

    fun setEmailError(@StringRes emailError: Int?) {
        if (emailErrorState == emailError) return
        savedStateHandle[emailErrorKey] = emailError
        emailErrorState = emailError
    }

    protected val emailErrorStream = snapshotFlow { emailErrorState }
    protected val buttonEnabledStream = snapshotFlow { buttonEnabledState }
}
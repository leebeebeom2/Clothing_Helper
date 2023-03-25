package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle

abstract class BaseSignInStateWithEmailAndPassword(
    private val savedStateHandle: SavedStateHandle,
    savedEmailKey: String,
    emailErrorKey: String,
    savedPasswordKey: String,
    private val passwordErrorKey: String
) : BaseSignInStateWithEmail(
    savedStateHandle = savedStateHandle,
    savedEmailKey = savedEmailKey,
    emailErrorKey = emailErrorKey
) {
    var savedPassword by SavedStateHandleDelegator(
        savedStateHandle = savedStateHandle,
        key = savedPasswordKey,
        initial = ""
    )
        protected set
    protected var passwordState by mutableStateOf(savedPassword)
    private var passwordErrorState by mutableStateOf(savedStateHandle.get<Int>(passwordErrorKey))
    override val buttonEnabledState by derivedStateOf { super.buttonEnabledState && passwordState.isNotBlank() && passwordErrorState == null }

    open fun setPassword(password: String) {
        if (passwordState == password) return
        savedPassword = password
        passwordState = password
        setPasswordError(null)
    }

    fun setPasswordError(@StringRes passwordError: Int?) {
        if (passwordErrorState == passwordError) return
        savedStateHandle[passwordErrorKey] = passwordError
        passwordErrorState = passwordError
    }

    protected val passwordErrorStream = snapshotFlow { passwordErrorState }
}
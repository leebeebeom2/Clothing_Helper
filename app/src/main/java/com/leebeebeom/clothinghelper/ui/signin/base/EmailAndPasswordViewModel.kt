package com.leebeebeom.clothinghelper.ui.signin.base

import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailAndPasswordUiState

abstract class EmailAndPasswordViewModel : EmailViewModel() {
    abstract override val mutableUiState: MutableEmailAndPasswordUiState

    open fun onPasswordChange(password: String) {
        mutableUiState.password = password
        mutableUiState.passwordError = null
    }
}
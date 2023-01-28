package com.leebeebeom.clothinghelper.ui.signin.base

import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.ui.signin.state.MutableEmailUiState

abstract class EmailViewModel : ViewModel() {
    protected abstract val mutableUiState: MutableEmailUiState

    fun onEmailChange(email: String) {
        mutableUiState.email = email
        mutableUiState.emailError = null
    }
}
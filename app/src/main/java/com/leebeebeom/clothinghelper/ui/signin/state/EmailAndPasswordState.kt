package com.leebeebeom.clothinghelper.ui.signin.state

import androidx.compose.runtime.*

@Stable
interface EmailAndPasswordState : EmailUiState {
    val passwordError: Int?
}

open class MutableEmailAndPasswordUiState : MutableEmailUiState(), EmailAndPasswordState {
    override var passwordError: Int? by mutableStateOf(null)
    var password by mutableStateOf("")
    override val buttonEnabled by derivedStateOf { super.buttonEnabled && password.isNotBlank() && passwordError == null }
}
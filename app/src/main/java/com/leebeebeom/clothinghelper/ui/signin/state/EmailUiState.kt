package com.leebeebeom.clothinghelper.ui.signin.state

import androidx.compose.runtime.*

@Stable
interface EmailUiState {
    val emailError: Int?
    val buttonEnabled: Boolean
}

open class MutableEmailUiState : EmailUiState {
    override var emailError: Int? by mutableStateOf(null)
    var email by mutableStateOf("")
    override val buttonEnabled by derivedStateOf { email.isNotBlank() && emailError == null }
}
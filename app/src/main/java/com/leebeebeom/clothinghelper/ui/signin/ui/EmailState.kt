package com.leebeebeom.clothinghelper.ui.signin.ui

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import com.leebeebeom.clothinghelper.ui.signin.state.LoadingState

abstract class EmailState(
    savedStateHandle: SavedStateHandle,
    emailKey: String,
    emailErrorKey: String
) : LoadingState() {
    val email = SavedStateProvider(
        savedStateHandle = savedStateHandle,
        key = emailKey,
        initialValue = ""
    )

    val emailError = SavedStateProvider<Int?>(
        savedStateHandle = savedStateHandle,
        key = emailErrorKey,
        initialValue = null
    )

    protected open val buttonEnabledState by derivedStateOf { email.state.isNotBlank() && emailError.state == null }

    fun setEmail(email: String) {
        if (this.email.state == email) return
        this.email.set(email)
        emailError.set(null)
    }

    protected val buttonEnabledFlow = snapshotFlow { buttonEnabledState }
}
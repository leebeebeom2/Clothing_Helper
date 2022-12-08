package com.leebeebeom.clothinghelper.ui.signin.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.state.ToastUIState
import com.leebeebeom.clothinghelper.state.ToastUIStateImpl

interface GoogleButtonUIState : ToastUIState {
    val googleButtonEnabled: Boolean

    fun updateGoogleButtonEnabled(enabled: Boolean)
}

/**
 * ToastUIState 상속
 */
class GoogleButtonUIStateImpl : GoogleButtonUIState, ToastUIState by ToastUIStateImpl() {
    override var googleButtonEnabled by mutableStateOf(true)
        private set

    override fun updateGoogleButtonEnabled(enabled: Boolean) {
        googleButtonEnabled = enabled
    }
}


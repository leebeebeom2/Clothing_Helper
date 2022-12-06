package com.leebeebeom.clothinghelper.signin.base.interfaces

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

interface GoogleButtonUIState {
    val googleButtonEnabled: Boolean

    fun updateGoogleButtonEnabled(enabled: Boolean)
}

class GoogleButtonUIStateImpl : GoogleButtonUIState {
    override var googleButtonEnabled by mutableStateOf(true)
        private set

    override fun updateGoogleButtonEnabled(enabled: Boolean) {
        googleButtonEnabled = enabled
    }
}


package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.leebeebeom.clothinghelper.base.BaseViewModelState

open class EmailViewModelState : BaseViewModelState() {
    var emailError: Int? by mutableStateOf(null)
        private set

    val showEmailError = { error: Int -> emailError = error }
    val hideEmailError = { emailError = null }
}

open class GoogleSignInViewModelState : EmailViewModelState() {
    var googleButtonEnabled by mutableStateOf(true)
        private set

    val setGoogleButtonEnable = { googleButtonEnabled = true }
    val setGoogleButtonDisable = { googleButtonEnabled = false }
}

interface TaskSuccessViewModelState {
    var taskSuccess: Boolean

    fun taskSuccess() {
        taskSuccess = true
    }

    fun popBackStackDone() {
        taskSuccess = false
    }
}
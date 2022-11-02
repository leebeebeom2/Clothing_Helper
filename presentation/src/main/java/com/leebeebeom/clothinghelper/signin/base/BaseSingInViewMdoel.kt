package com.leebeebeom.clothinghelper.signin.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.base.BaseUIState
import com.leebeebeom.clothinghelper.base.BaseViewModel

abstract class BaseSignInViewModel : BaseViewModel() {
    abstract fun updateEmailError(@StringRes error: Int?)
}

abstract class BaseSignInUIState : BaseUIState() {
    abstract val isNotError: Boolean
}
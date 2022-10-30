package com.leebeebeom.clothinghelper.signin.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.base.BaseViewModelState
import com.leebeebeom.clothinghelperdomain.usecase.user.BaseSignInUseCase
import kotlinx.coroutines.launch

abstract class BaseSignInViewModel(private val baseSignInUseCase: BaseSignInUseCase) : ViewModel() {
    abstract val viewModelState: BaseViewModelState

    init {
        viewModelScope.launch {
            baseSignInUseCase.isLoading.collect(viewModelState::updateLoadingState)
        }
    }
}
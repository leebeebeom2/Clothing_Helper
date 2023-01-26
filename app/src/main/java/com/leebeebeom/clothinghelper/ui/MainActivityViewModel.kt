package com.leebeebeom.clothinghelper.ui

import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.domain.usecase.user.GetSignInStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val getSignInStateUseCase: GetSignInStateUseCase) :
    ViewModel() {
    val isSignIn get() = getSignInStateUseCase.isSignIn
}
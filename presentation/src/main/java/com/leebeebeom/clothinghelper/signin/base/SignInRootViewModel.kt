package com.leebeebeom.clothinghelper.signin.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.usecase.user.GetSignInLoadingStateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInRootViewModel @Inject constructor(private val getSignInLoadingStateUseCase: GetSignInLoadingStateUseCase) :
    ViewModel() {
    var isLoading by mutableStateOf(false)

    init {
        viewModelScope.launch {
            getSignInLoadingStateUseCase().collect {
                isLoading = it
            }
        }
    }
}


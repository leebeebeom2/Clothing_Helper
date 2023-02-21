package com.leebeebeom.clothinghelper.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.FirebaseAuthErrorUseCase
import com.leebeebeom.clothinghelper.domain.usecase.user.SignOutUseCase
import com.leebeebeom.clothinghelper.ui.util.ShowToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val firebaseAuthErrorUseCase: FirebaseAuthErrorUseCase,
) :
    ViewModel() {
    fun signOut(showToast: ShowToast) = viewModelScope.launch {
        signOutUseCase.signOut(
            onFail = {
                firebaseAuthErrorUseCase.firebaseAuthError(
                    exception = it,
                    showToast = showToast
                )
            },
        )
    }
}
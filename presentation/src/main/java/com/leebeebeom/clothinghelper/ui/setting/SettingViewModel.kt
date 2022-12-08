package com.leebeebeom.clothinghelper.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelperdomain.usecase.user.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val signOutUseCase: SignOutUseCase) :
    ViewModel() {
    fun signOut() = viewModelScope.launch { signOutUseCase.signOut() }
}
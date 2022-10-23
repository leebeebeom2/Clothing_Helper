package com.leebeebeom.clothinghelper.main.setting

import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelperdomain.usecase.user.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(signOutUseCase: SignOutUseCase) : ViewModel() {
    val signOut = { signOutUseCase() }
}
package com.leebeebeom.clothinghelper.ui.signin

import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.data.UserRepository

class SignInViewModel : ViewModel() {
    val isLogin get() = UserRepository.isLogin
}
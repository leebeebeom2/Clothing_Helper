package com.leebeebeom.clothinghelper.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leebeebeom.clothinghelper.domain.usecase.user.UserInfoUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userInfoUserCase: UserInfoUserCase
) : ViewModel() {

    var viewModelState = MainViewModelState(
        userInfoUserCase.isLogin.value,
        userInfoUserCase.name.value,
        userInfoUserCase.email.value
    )

    init {
        viewModelScope.launch {
            userInfoUserCase.isLogin.collect(viewModelState::isLogin)
        }
        viewModelScope.launch {
            userInfoUserCase.name.collect(viewModelState::updateName)
        }
        viewModelScope.launch {
            userInfoUserCase.email.collect(viewModelState::updateEmail)
        }
    }
}

class MainViewModelState(
    initialLoginState: Boolean,
    initialName: String,
    initialEmail: String,
) {
    var isLogin by mutableStateOf(initialLoginState)
        private set
    var name by mutableStateOf(initialName)
        private set
    var email by mutableStateOf(initialEmail)
        private set

    fun isLogin(isLogin: Boolean) {
        this.isLogin = isLogin
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updateEmail(email: String) {
        this.email = email
    }
}
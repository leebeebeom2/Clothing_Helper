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

    var isLogin by mutableStateOf(userInfoUserCase.isLogin.value)
        private set

    init {
        viewModelScope.launch {
            userInfoUserCase.isLogin.collect { isLogin = it }
        }
    }
}
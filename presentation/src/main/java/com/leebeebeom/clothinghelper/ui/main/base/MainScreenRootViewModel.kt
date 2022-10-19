package com.leebeebeom.clothinghelper.ui.main.base

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
class MainScreenRootViewModel @Inject constructor(
    private val userInfoUserCase: UserInfoUserCase
) : ViewModel() {
    val viewModelState =
        MainNavHostViewModelState(userInfoUserCase.name.value, userInfoUserCase.email.value)

    init {
        viewModelScope.launch {
            userInfoUserCase.name.collect { viewModelState.nameUpdate(it) }
        }
    }
}

class MainNavHostViewModelState(
    name: String,
    email: String
) {
    var name by mutableStateOf(name)
        private set
    var email by mutableStateOf(email)
        private set

    fun nameUpdate(name: String) {
        this.name = name
    }
}
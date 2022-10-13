package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.leebeebeom.clothinghelper.data.UserRepository

open class MainViewModel : ViewModel() {
    var mainState by mutableStateOf(MainUIState())
        private set

    val loadingOn = { mainState = mainState.loadingOn() }
    val loadingOff = { mainState = mainState.loadingOff() }
}

data class MainUIState(
    val isLoading: Boolean = false
) {
    val isLogin get() = UserRepository.isLogin
    val userName get() = UserRepository.userName
    val userEmail get() = UserRepository.userEmail

    fun loadingOn() = copy(isLoading = true)
    fun loadingOff() = copy(isLoading = false)
}
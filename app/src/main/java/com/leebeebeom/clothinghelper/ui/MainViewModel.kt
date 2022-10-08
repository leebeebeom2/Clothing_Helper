package com.leebeebeom.clothinghelper.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {
    var isLogin by mutableStateOf(false)
        private set

    private val listener: (FirebaseAuth) -> Unit = { isLogin = it.currentUser != null }

    init {
        FirebaseAuth.getInstance().addAuthStateListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        FirebaseAuth.getInstance().removeAuthStateListener(listener)
    }
}
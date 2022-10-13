package com.leebeebeom.clothinghelper.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object UserRepository {
    var user: FirebaseUser? by mutableStateOf(null)
        private set

    var isLogin by mutableStateOf(false)
        private set

    var userName by mutableStateOf("")
        private set
    var userEmail by mutableStateOf("")
        private set

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            user = it.currentUser
            isLogin = user != null
            user?.run {
                userName = displayName ?: ""
                userEmail = email ?: ""
            }
        }
    }

    fun userNameUpdate(name: String) {
        userName = name
    }
}
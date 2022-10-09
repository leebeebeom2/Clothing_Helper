package com.leebeebeom.clothinghelper.ui.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.leebeebeom.clothinghelper.ui.LoginViewModel

open class MainViewModel : LoginViewModel() {
    //TODO 유저 네임 업데이트
    val name by mutableStateOf(FirebaseAuth.getInstance().currentUser?.displayName)
    val email by mutableStateOf(FirebaseAuth.getInstance().currentUser?.email)
    val userNameAndEmail get() = "$name($email)"
}
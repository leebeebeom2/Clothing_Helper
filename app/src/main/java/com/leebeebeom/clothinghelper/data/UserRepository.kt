package com.leebeebeom.clothinghelper.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object UserRepository {
    private var user: FirebaseUser? by mutableStateOf(FirebaseAuth.getInstance().currentUser)

    var isLogin by mutableStateOf(user != null)
        private set

    var userName: String? by mutableStateOf(user?.displayName)
        private set
    var userEmail: String? by mutableStateOf(user?.email)
        private set

    val userNameAndEmail
        get() =
            if (userName != null && userEmail != null) "$userName($userEmail)"
            else null

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            user = it.currentUser
            isLogin = user != null
            user?.run {
                userName = displayName
                userEmail = email
            }
        }
    }

    fun userNameUpdate(name: String) {
        userName = name
    }
}
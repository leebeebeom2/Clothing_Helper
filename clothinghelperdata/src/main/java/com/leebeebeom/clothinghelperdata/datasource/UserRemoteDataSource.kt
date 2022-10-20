package com.leebeebeom.clothinghelperdata.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserRemoteDataSource {
    val auth = FirebaseAuth.getInstance()
    private var _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> get() = _user

    private val listener = FirebaseAuth.AuthStateListener {
        _user.value = it.currentUser
    }

    init {
        auth.addAuthStateListener(listener)
    }
}
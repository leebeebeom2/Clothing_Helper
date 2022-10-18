package com.leebeebeom.clothinghelper.data.datasource

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.leebeebeom.clothinghelper.data.model.User

class ContentRemoteDataSource {
    private val database = Firebase.database.reference

    private fun writeUser(email: String, name: String) {
        val user = User(email, name)
        database.child("users").child(email).setValue(user)
    }
}
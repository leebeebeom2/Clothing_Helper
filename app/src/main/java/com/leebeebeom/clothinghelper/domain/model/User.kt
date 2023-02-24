package com.leebeebeom.clothinghelper.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String = "",
    val name: String = "",
) : Parcelable

data class FirebaseUser(
    val email: String = "",
    val name: String = "",
    val uid: String = "",
)

fun FirebaseUser.toUser() = User(email = email, name = name)
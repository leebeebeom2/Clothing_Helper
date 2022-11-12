package com.leebeebeom.clothinghelper.map

import com.leebeebeom.clothinghelperdomain.model.User

data class StableUser(
    val email: String,
    val name: String,
    val uid: String
)

fun User.toStable(): StableUser {
    return StableUser(email = email, name = name, uid = uid)
}
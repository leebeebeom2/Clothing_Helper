package com.leebeebeom.clothinghelperdomain.model

data class User(
    val email: String,
    val name: String,
    val uid: String
)

data class SignIn(
    val email: String,
    val password: String
)

data class SignUp(
    val email:String,
    val password: String,
    val name: String
)
package com.leebeebeom.clothinghelper.domain.model

sealed class FirebaseResult {
    object Success : FirebaseResult()
    data class Fail(val exception: Exception) : FirebaseResult()
}

sealed class AuthResult {
    object Success : AuthResult()

    data class Fail(val errorCode: String) : AuthResult()
    object UnknownFail : AuthResult()
}
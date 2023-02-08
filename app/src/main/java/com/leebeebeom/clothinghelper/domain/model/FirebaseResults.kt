package com.leebeebeom.clothinghelper.domain.model

import com.leebeebeom.clothinghelper.domain.model.data.User

sealed class FirebaseResult {
    object Success : FirebaseResult()
    data class Fail(val exception: Exception) : FirebaseResult()
}

sealed class AuthResult {
    object EmptySuccess : AuthResult()
    data class Success(
        val user: User = User(),
        val isNewer: Boolean = false,
    ) : AuthResult()

    data class Fail(val errorCode: String) : AuthResult()
    object UnknownFail : AuthResult()
}
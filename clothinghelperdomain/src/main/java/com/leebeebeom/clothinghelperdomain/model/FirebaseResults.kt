package com.leebeebeom.clothinghelperdomain.model

import com.leebeebeom.clothinghelperdomain.model.data.User

sealed class FirebaseResult {
    object Success : FirebaseResult()
    data class Fail(val exception: Exception) : FirebaseResult()
}

sealed class AuthResult {
    data class Success(val user: User = User(), val isNewer: Boolean = false) : AuthResult()
    data class Fail(val errorCode: String) : AuthResult()
    object UnknownFail : AuthResult()
}
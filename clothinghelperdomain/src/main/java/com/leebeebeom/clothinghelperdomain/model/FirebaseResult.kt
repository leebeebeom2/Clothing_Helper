package com.leebeebeom.clothinghelperdomain.model

sealed class FirebaseResult {
    object Success : FirebaseResult()
    data class Fail(val exception: Exception?) : FirebaseResult()
}
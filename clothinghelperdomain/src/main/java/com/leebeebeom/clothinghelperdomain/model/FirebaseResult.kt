package com.leebeebeom.clothinghelperdomain.model

sealed class FirebaseResult {
    object Success : FirebaseResult()
    data class Fail(val exception: Exception?) : FirebaseResult()
}

sealed class AuthResult {
    data class Success(val user: User, val isNewer: Boolean) : AuthResult()
    data class Fail(val exception: Exception?) : AuthResult()
}

sealed class SubCategoryPushResult {
    data class Success(val subCategory: SubCategory) : SubCategoryPushResult()
    data class Fail(val exception: Exception?) : SubCategoryPushResult()
}
package com.leebeebeom.clothinghelper.data.repository.util

data class AuthCallSite(private val callSite: String) {
    val site = "UserRepositoryImpl: $callSite"
}

data class DatabaseCallSite(private val callSite: String) {
    val site = "BaseDataRepositoryImpl: $callSite"
}
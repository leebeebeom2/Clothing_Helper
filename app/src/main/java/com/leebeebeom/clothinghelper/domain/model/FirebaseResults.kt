package com.leebeebeom.clothinghelper.domain.model

interface FirebaseResult {
    fun success()
    fun fail(exception: Exception)
}
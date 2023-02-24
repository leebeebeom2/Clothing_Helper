package com.leebeebeom.clothinghelper.data.repository

interface FirebaseResult {
    fun success()
    fun fail(exception: Exception)
}
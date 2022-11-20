package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult

interface ContainerRepository<T> {
    suspend fun load(uid: String?, type: Class<T>): FirebaseResult
    suspend fun add(t: T, uid: String): FirebaseResult
    suspend fun edit(t: T, uid: String): FirebaseResult
}
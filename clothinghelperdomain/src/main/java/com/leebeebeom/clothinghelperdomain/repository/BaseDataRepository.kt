package com.leebeebeom.clothinghelperdomain.repository

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import kotlinx.coroutines.flow.Flow

interface BaseDataRepository<T> {
    val allData: Flow<List<T>>
    suspend fun load(uid: String?, type: Class<T>): FirebaseResult
    suspend fun add(t: T, uid: String): FirebaseResult
    suspend fun edit(newT: T, uid: String): FirebaseResult
}
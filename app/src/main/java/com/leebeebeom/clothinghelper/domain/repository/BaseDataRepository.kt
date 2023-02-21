package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import kotlinx.coroutines.flow.Flow

interface BaseDataRepository<T> : LoadingStateProvider {
    val allData: Flow<List<T>>
    suspend fun load(uid: String?, type: Class<T>, firebaseResult: FirebaseResult)
    suspend fun add(data: T, uid: String, firebaseResult: FirebaseResult)
    suspend fun edit(newData: T, uid: String, firebaseResult: FirebaseResult)
}
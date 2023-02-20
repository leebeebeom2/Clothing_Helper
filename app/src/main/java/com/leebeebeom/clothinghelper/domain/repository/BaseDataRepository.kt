package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import kotlinx.coroutines.flow.Flow

interface BaseDataRepository<T> : LoadingStateProvider {
    val allData: Flow<List<T>>

    suspend fun load(uid: String?, type: Class<T>): FirebaseResult
    suspend fun add(data: T, uid: String): FirebaseResult
    suspend fun edit(newData: T, uid: String): FirebaseResult
}
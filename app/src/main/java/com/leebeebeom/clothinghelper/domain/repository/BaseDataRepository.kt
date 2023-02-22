package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import kotlinx.coroutines.flow.Flow

interface BaseDataRepository<T> : LoadingStateProvider {
    val allData: Flow<List<T>>
    suspend fun load(uid: String?, type: Class<T>, onFail: (Exception) -> Unit)
    suspend fun add(data: T, uid: String, onFail: (Exception) -> Unit)
    suspend fun edit(newData: T, uid: String, onFail: (Exception) -> Unit)
    suspend fun getAllAsync(): List<T>
}
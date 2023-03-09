package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.FirebaseNetworkException
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import kotlinx.coroutines.flow.SharedFlow

interface BaseDataRepository<T : BaseModel> : LoadingStateProvider {
    val allData: SharedFlow<List<T>>

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     */
    suspend fun add(data: T)

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     */
    suspend fun push(data: T)
}
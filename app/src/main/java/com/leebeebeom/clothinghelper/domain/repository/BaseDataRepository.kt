package com.leebeebeom.clothinghelper.domain.repository

import com.google.firebase.FirebaseNetworkException
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.data.repository.util.WifiException
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow

interface BaseDataRepository<T : BaseDatabaseModel> : LoadingStateProvider {
    val allData: StateFlow<List<T>>
    suspend fun load(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        uid: String?,
        type: Class<T>,
        onFail: (Exception) -> Unit,
    )

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     */
    suspend fun add(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        data: T,
        uid: String,
        onFail: (Exception) -> Unit,
    )

    /**
     * @throws FirebaseNetworkException 인터넷 미 연결 시
     * @throws WifiException 사용자가 와이파이로만 연결 선택 시 와이파이 미 연결됐을 경우
     * @throws NoSuchElementException 본래 데이터를 찾지 못했을 경우
     * @throws IllegalArgumentException 본래 데이터를 삭제하지 못했을 경우
     */
    suspend fun edit(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    )
}
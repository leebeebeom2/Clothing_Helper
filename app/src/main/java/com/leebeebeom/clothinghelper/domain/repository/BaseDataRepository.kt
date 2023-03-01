package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
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

    suspend fun add(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        data: T,
        uid: String,
        onFail: (Exception) -> Unit,
    )

    suspend fun edit(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        newData: T,
        uid: String,
        onFail: (Exception) -> Unit,
    )
}
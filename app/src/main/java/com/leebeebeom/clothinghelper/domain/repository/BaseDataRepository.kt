package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.repository.DataResult
import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import kotlinx.coroutines.flow.SharedFlow

interface BaseDataRepository<T : BaseModel> : LoadingStateProvider {
    val allData: SharedFlow<DataResult<T>>

    suspend fun add(data: T)

    suspend fun push(data: T)
}
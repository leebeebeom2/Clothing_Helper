package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import kotlinx.coroutines.flow.SharedFlow

interface BaseDataRepository<T : BaseModel> {
    val allDataFlow: SharedFlow<DataResult<T>>
    suspend fun add(data: T)
    suspend fun push(data: T)
}

sealed class DataResult<T>(val data: List<T>) {
    class Success<T>(data: List<T>) : DataResult<T>(data)
    class Fail<T>(data: List<T>, val exception: Throwable) : DataResult<T>(data)
}
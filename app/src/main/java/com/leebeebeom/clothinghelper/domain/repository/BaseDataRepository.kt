package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.SharedFlow

interface BaseDataRepository<T : BaseModel> {
    val allDataFlow: SharedFlow<DataResult<T>>
    val allDataSizeFlow: SharedFlow<Int>
    suspend fun add(data: T)
    suspend fun push(data: T)
}

sealed class DataResult<T>(val data: ImmutableList<T>) {
    class Success<T>(data: ImmutableList<T>) : DataResult<T>(data)
    class Fail<T>(data: ImmutableList<T>, val exception: Throwable) : DataResult<T>(data)
}
package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharedFlow

interface BaseDataRepository<T : BaseModel> : LoadingStateProvider {
    val allDataStream: SharedFlow<DataResult<T>>
    suspend fun add(data: T): Job
    suspend fun push(data: T): Job
}

sealed class DataResult<T> {
    data class Success<T>(val data: List<T>) : DataResult<T>()
    data class Fail<T>(val data: List<T>, val exception: Throwable) : DataResult<T>()
}
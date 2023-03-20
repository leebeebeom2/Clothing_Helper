package com.leebeebeom.clothinghelper.domain.repository

import com.leebeebeom.clothinghelper.data.repository.util.LoadingStateProvider
import com.leebeebeom.clothinghelper.domain.model.BaseModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow

interface BaseDataRepository<T : BaseModel> : LoadingStateProvider {
    val allDataStream: StateFlow<List<T>>

    suspend fun add(data: T): Job

    suspend fun push(data: T): Job
}
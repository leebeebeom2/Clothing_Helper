package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import kotlinx.coroutines.flow.Flow

abstract class BaseRoomDataSource<T : BaseModel> {
    abstract fun getAll(): Flow<List<T>>
    abstract suspend fun deleteAll()
    abstract suspend fun insert(data: T)
    abstract suspend fun insert(data: List<T>)
    abstract suspend fun update(data: T)
}
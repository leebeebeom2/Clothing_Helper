package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.domain.model.data.BaseModel
import kotlinx.coroutines.flow.Flow

abstract class BaseRoomDataSource<T : BaseModel>(private val dao: BaseDao<T>) {
    abstract fun getAll(): Flow<List<T>>
    abstract suspend fun getAllAsync(): List<T>
    abstract suspend fun deleteAll()
    suspend fun insert(data: T) = dao.insert(data)
    suspend fun insert(data: List<T>) = dao.insert(data)
    suspend fun update(data: T) = dao.update(data)
    suspend fun update(data: List<T>) = dao.update(data)
}
package com.leebeebeom.clothinghelper.data.datasourse

import com.leebeebeom.clothinghelper.domain.model.BaseModel
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseModel
import kotlinx.coroutines.flow.Flow

abstract class BaseRoomDataSource<T : BaseDatabaseModel, U : BaseModel>(private val dao: BaseDao<T>) {
    abstract fun getAll(): Flow<List<U>>
    abstract suspend fun deleteAll()
    suspend fun insert(data: T) = dao.insert(data)
    suspend fun insert(data: List<T>) = dao.insert(data)
    suspend fun update(data: T) = dao.update(data)
}
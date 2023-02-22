package com.leebeebeom.clothinghelper.data.datasourse

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface BaseDao<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getAllAsync(): List<T>
    suspend fun deleteAll()

    @Insert
    suspend fun insert(data: T)

    @Insert
    suspend fun insert(data: List<T>)

    @Update
    suspend fun update(data: T)

    @Update
    suspend fun update(data: List<T>)

    @Delete
    suspend fun delete(data: T)
}
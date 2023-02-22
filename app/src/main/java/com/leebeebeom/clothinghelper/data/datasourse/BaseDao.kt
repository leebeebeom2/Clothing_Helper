package com.leebeebeom.clothinghelper.data.datasourse

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {
    @Insert
    suspend fun insert(data: T)

    @Insert
    suspend fun insert(data: List<T>)

    @Update
    suspend fun update(data: T)

    @Delete
    suspend fun delete(data: T)
}
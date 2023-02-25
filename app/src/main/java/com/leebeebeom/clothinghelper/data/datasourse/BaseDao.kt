package com.leebeebeom.clothinghelper.data.datasourse

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.leebeebeom.clothinghelper.domain.model.BaseDatabaseModel

interface BaseDao<T : BaseDatabaseModel> {
    @Insert
    suspend fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: List<T>)

    @Update
    suspend fun update(data: T)

    @Update
    suspend fun update(data: List<T>)

    @Delete
    suspend fun delete(data: T)
}
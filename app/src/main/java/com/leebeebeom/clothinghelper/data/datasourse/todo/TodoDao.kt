package com.leebeebeom.clothinghelper.data.datasourse.todo

import androidx.room.Dao
import androidx.room.Query
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.data.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao : BaseDao<Todo> {
    @Query("SELECT * FROM Todo")
    override fun getAll(): Flow<List<Todo>>

    @Query("SELECT * FROM Todo WHERE isSynced = false")
    override fun getAllAsync(): Flow<List<Todo>>

    @Query("DELETE FROM Todo")
    override suspend fun deleteAll()
}
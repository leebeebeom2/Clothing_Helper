package com.leebeebeom.clothinghelper.data.datasourse.todo

import androidx.room.Dao
import androidx.room.Query
import com.leebeebeom.clothinghelper.data.datasourse.BaseDao
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao : BaseDao<DatabaseTodo> {
    @Query("SELECT * FROM DatabaseTodo")
    fun getAll(): Flow<List<Todo>>

    @Query("DELETE FROM DatabaseTodo")
    suspend fun deleteAll()
}
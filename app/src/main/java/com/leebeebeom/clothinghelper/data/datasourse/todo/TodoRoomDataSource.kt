package com.leebeebeom.clothinghelper.data.datasourse.todo

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.DatabaseTodo
import com.leebeebeom.clothinghelper.domain.model.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRoomDataSource @Inject constructor(private val dao: TodoDao) :
    BaseRoomDataSource<DatabaseTodo, Todo>(dao = dao) {
    override fun getAll() = dao.getAll()
    override suspend fun deleteAll() = dao.deleteAll()
}
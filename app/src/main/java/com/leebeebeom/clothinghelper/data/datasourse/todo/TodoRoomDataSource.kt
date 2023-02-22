package com.leebeebeom.clothinghelper.data.datasourse.todo

import com.leebeebeom.clothinghelper.data.datasourse.BaseRoomDataSource
import com.leebeebeom.clothinghelper.domain.model.data.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRoomDataSource @Inject constructor(private val dao: TodoDao) :
    BaseRoomDataSource<Todo>(dao = dao) {
    override fun getAll() = dao.getAll()
    override suspend fun getAllAsync() = dao.getAllAsync()
    override suspend fun deleteAll() = dao.deleteAll()
}
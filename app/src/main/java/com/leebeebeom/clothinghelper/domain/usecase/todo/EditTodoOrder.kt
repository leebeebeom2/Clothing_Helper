package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class EditTodoOrder @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun editOrder(
        oldTodo: Todo,
        order: Int,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newTodo = oldTodo.copy(order = order)

        todoRepository.edit(oldData = oldTodo, newData = newTodo, uid = uid, onFail = onFail)
    }
}
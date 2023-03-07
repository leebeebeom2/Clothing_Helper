package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class EditTodoTextUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun editText(
        oldTodo: Todo,
        text: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newTodo = oldTodo.copy(text = text)
        todoRepository.edit(oldData = oldTodo, newData = newTodo, uid = uid, onFail = onFail)
    }
}
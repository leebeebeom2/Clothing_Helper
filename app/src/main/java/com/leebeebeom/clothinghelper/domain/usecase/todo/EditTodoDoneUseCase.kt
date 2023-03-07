package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class EditTodoDoneUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun editDone(
        oldTodo: Todo,
        done: Boolean,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newTodo = oldTodo.copy(done = done)

        todoRepository.edit(
            oldData = oldTodo,
            newData = newTodo,
            uid = uid,
            onFail = onFail
        )
    }
}
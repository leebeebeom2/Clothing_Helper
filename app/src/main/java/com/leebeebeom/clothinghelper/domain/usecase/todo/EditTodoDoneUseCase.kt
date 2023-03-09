package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class EditTodoDoneUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun editDone(oldTodo: Todo, done: Boolean) {
        val newTodo = oldTodo.copy(done = done)

        todoRepository.push(data = newTodo)
    }
}
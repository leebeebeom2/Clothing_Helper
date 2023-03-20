package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class EditTodoOrderUseCase @Inject constructor(private val todoRepository: TodoRepository) {
    suspend fun editOrder(oldTodo: Todo, order: Int) =
        todoRepository.push(data = oldTodo.copy(order = order))
}
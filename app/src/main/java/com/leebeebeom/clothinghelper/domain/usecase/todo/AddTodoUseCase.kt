package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(private val todoRepository: TodoRepository) {
    suspend fun add(text: String, done: Boolean, order: Int) {
        val todo = Todo(text = text, done = done, order = order)

        todoRepository.add(data = todo)
    }
}
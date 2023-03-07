package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.model.Todo
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class AddTodoUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    suspend fun add(
        uid: String,
        text: String,
        done: Boolean,
        order: Int,
        onFail: (Exception) -> Unit,
    ) {
        val todo = Todo(text = text, done = done, order = order)

        todoRepository.add(todo, uid, onFail = onFail)
    }
}
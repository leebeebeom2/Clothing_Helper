package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class GetAllTodosStreamUseCase @Inject constructor(private val todoRepository: TodoRepository) {
    val allTodosStream get() = todoRepository.allDataStream
}
package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class GetAllTodosFlowUseCase @Inject constructor(private val todoRepository: TodoRepository) {
    val allTodosFlow get() = todoRepository.allDataFlow
}
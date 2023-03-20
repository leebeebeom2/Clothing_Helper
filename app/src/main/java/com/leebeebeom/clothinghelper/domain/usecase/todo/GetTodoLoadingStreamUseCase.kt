package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoLoadingStreamUseCase @Inject constructor(private val todoRepository: TodoRepository) {
    val todoLoadingStream get() = todoRepository.loadingStream
}
package com.leebeebeom.clothinghelper.domain.usecase.todo

import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import javax.inject.Inject

class GetIsTodoLoadingStateUseCase @Inject constructor(
    private val todoRepository: TodoRepository,
) {
    val isLoading get() = todoRepository.isLoading
}
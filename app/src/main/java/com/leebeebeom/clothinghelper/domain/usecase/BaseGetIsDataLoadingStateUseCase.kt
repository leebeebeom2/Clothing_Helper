package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.repository.BaseDataRepository

abstract class BaseGetIsDataLoadingStateUseCase(
    private val repository: BaseDataRepository<*>,
) {
    val isLoading get() = repository.isLoading
}
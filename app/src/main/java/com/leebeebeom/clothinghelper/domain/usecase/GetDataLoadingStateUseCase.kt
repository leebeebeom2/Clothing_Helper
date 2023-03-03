package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class GetDataLoadingStateUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
    private val todoRepository: TodoRepository,
) {
    fun isDataLoading(scope: CoroutineScope) =
        combine(
            flow = subCategoryRepository.isLoading,
            flow2 = folderRepository.isLoading,
            flow3 = todoRepository.isLoading
        ) { isSubCategoryLoading, isFolderLoading, isTodoLoading ->
            isSubCategoryLoading && isFolderLoading && isTodoLoading
        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = true
        )
}
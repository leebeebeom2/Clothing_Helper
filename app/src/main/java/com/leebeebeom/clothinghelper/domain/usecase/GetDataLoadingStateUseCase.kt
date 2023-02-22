package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDataLoadingStateUseCase @Inject constructor(
    subCategoryRepository: SubCategoryRepository,
    folderRepository: FolderRepository,
    todoRepository: TodoRepository
) {
    var isLoading: Flow<Boolean>
        private set

    init {
        isLoading = combine(
            flow = subCategoryRepository.isLoading,
            flow2 = folderRepository.isLoading,
            flow3 = todoRepository.isLoading
        ) { isSubCategoryLoading, isFolderLoading, isTodoLoading ->
            isSubCategoryLoading && isFolderLoading && isTodoLoading
        }
    }
}
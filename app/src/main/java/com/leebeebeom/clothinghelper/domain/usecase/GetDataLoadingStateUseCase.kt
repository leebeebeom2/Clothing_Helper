package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.repository.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDataLoadingStateUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
    private val todoRepository: TodoRepository,
    @AppScope private val appScope: CoroutineScope,
) {
    private lateinit var isLoading: StateFlow<Boolean>

    fun getIsLoading(): StateFlow<Boolean> {
        if (!::isLoading.isInitialized)
            isLoading = combine(
                flow = subCategoryRepository.isLoading,
                flow2 = folderRepository.isLoading,
                flow3 = todoRepository.isLoading
            ) { isSubCategoryLoading, isFolderLoading, isTodoLoading ->
                isSubCategoryLoading && isFolderLoading && isTodoLoading
            }.stateIn(
                scope = appScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = true
            )
        return isLoading
    }
}
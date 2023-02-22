package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class AllLocalDataClearUseCase @Inject constructor(
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
) {
    suspend fun allLocalDataClear(
        onSubCategoryClearFail: (Exception) -> Unit,
        onFolderClearFail: (Exception) -> Unit,
    ) {
        coroutineScope {
            launch { subCategoryRepository.allLocalDataClear(onSubCategoryClearFail) }
            launch { folderRepository.allLocalDataClear(onFolderClearFail) }
        }
    }
}
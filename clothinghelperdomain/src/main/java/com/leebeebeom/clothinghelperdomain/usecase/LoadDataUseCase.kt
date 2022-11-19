package com.leebeebeom.clothinghelperdomain.usecase

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelperdomain.usecase.user.GetUserUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ViewModelScoped
class LoadDataUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository
) {
    suspend fun load(onUpdateSubCategoriesFail: (FirebaseResult) -> Unit) =
        getUserUseCase.user.collectLatest {
            coroutineScope {
                val subCategoryDeferred = async { subCategoryRepository.loadSubCategories(it?.uid) }
                val folderDeferred = async { folderRepository.loadFolders(it?.uid) }

                val subCategoryResult = subCategoryDeferred.await()
                val folderResult = folderDeferred.await()

                when {
                    subCategoryResult is FirebaseResult.Fail -> onUpdateSubCategoriesFail(subCategoryResult)
                    folderResult is FirebaseResult.Fail -> onUpdateSubCategoriesFail(folderResult)
                }
            }
        }
}
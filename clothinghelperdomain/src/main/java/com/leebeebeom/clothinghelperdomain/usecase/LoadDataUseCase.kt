package com.leebeebeom.clothinghelperdomain.usecase

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.container.Folder
import com.leebeebeom.clothinghelperdomain.model.container.SubCategory
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
    /**
     * 유저 변경 시 서브 카테고리, 폴더 리로드
     */
    suspend fun load(onUpdateSubCategoriesFail: (FirebaseResult) -> Unit) =
        getUserUseCase.user.collectLatest {
            coroutineScope {
                val subCategoryDeferred =
                    async { subCategoryRepository.load(it?.uid, SubCategory::class.java) }
                val folderDeferred = async { folderRepository.load(it?.uid, Folder::class.java) }

                val subCategoryResult = subCategoryDeferred.await()
                val folderResult = folderDeferred.await()

                when {
                    subCategoryResult is FirebaseResult.Fail ->
                        onUpdateSubCategoriesFail(subCategoryResult)
                    folderResult is FirebaseResult.Fail -> onUpdateSubCategoriesFail(folderResult)
                }
            }
        }
}
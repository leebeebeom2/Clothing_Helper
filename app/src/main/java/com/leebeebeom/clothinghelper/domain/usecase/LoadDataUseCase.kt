package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ViewModelScoped
class LoadDataUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
) {
    suspend fun load(onLoadDataFail: (FirebaseResult.Fail) -> Unit) =
        getUserUseCase.user.collectLatest {
            coroutineScope {
                val subCategoryDeferred = async {
                    subCategoryRepository.load(
                        uid = it?.uid,
                        type = SubCategory::class.java
                    )
                }
                val folderDeferred =
                    async { folderRepository.load(uid = it?.uid, type = Folder::class.java) }

                val subCategoryResult = subCategoryDeferred.await()
                val folderResult = folderDeferred.await()

                when {
                    subCategoryResult is FirebaseResult.Fail -> onLoadDataFail(subCategoryResult)
                    folderResult is FirebaseResult.Fail -> onLoadDataFail(folderResult)
                }
            }
        }
}
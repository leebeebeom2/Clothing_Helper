package com.leebeebeom.clothinghelper.domain.usecase

import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.domain.model.data.SubCategory
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.domain.repository.SubCategoryRepository
import com.leebeebeom.clothinghelper.domain.usecase.user.GetUserUseCase
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@ViewModelScoped
class LoadDataUseCase @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val subCategoryRepository: SubCategoryRepository,
    private val folderRepository: FolderRepository,
) {
    suspend fun load(
        subCategoryLoadFail: (Exception) -> Unit,
        folderLoadFail: (Exception) -> Unit,
    ) = getUserUseCase.user.collectLatest {// TODO 속도 테스트
        subCategoryRepository.load(
            uid = it?.uid,
            type = SubCategory::class.java,
            onFail = subCategoryLoadFail
        )

        folderRepository.load(
            uid = it?.uid,
            type = Folder::class.java,
            onFail = folderLoadFail
        )
    }
}
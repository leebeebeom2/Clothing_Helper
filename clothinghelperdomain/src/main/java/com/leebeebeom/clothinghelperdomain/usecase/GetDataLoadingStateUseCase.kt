package com.leebeebeom.clothinghelperdomain.usecase

import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDataLoadingStateUseCase @Inject constructor(
    subCategoryRepository: SubCategoryRepository,
    folderRepository: FolderRepository
) {
    val isLoading = combine(
        subCategoryRepository.isLoading,
        folderRepository.isLoading
    ) { isSubCategoryLoading, isFolderLoading ->
        isSubCategoryLoading && isFolderLoading
    }
}
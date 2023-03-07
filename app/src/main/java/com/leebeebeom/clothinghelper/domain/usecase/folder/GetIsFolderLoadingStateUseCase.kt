package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetIsFolderLoadingStateUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val isLoading get() = folderRepository.isLoading
}
package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetAllFoldersFlowUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val allFoldersFlow get() = folderRepository.allDataFlow
}
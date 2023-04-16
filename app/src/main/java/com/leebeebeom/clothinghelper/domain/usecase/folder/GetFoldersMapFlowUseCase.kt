package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetFoldersMapFlowUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    val foldersMapFlow get() = folderRepository.allFoldersMapFlow
}
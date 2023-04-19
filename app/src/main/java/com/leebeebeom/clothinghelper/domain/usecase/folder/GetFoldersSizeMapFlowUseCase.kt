package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetFoldersSizeMapFlowUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    val foldersSizeMapFlow get() = folderRepository.foldersSizeMapFlow
}
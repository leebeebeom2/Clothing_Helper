package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetFolderSizeMapFlowUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    val folderSizeMapFolder get() = folderRepository.dataSizeMapFlow
}
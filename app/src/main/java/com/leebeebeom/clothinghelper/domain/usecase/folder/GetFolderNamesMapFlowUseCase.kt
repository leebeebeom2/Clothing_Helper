package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetFolderNamesMapFlowUseCase @Inject constructor(
    private val folderRepository: FolderRepository
) {
    val folderNamesMapFlow get() = folderRepository.folderNamesMapFlow
}
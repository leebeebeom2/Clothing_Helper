package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetAllFoldersUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val allFolders get() = folderRepository.allDataStream
}
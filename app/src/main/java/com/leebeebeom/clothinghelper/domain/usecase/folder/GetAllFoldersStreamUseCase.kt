package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetAllFoldersStreamUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val allFoldersStream get() = folderRepository.allDataStream
}
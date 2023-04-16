package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun deletedFolder(folder: Folder) {
        val deletedFolder = folder.copy(isDeleted = true)

        folderRepository.push(deletedFolder)
    }
}
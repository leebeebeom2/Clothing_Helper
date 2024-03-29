package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class DeleteFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun delete(folder: Folder) {
        val deletedFolder = folder.copy(deleted = true)

        folderRepository.push(deletedFolder)
    }
}
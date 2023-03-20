package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class EditFolderNameUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun editName(oldFolder: Folder, name: String) =
        folderRepository.push(data = oldFolder.copy(name = name))
}
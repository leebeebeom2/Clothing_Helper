package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class EditFolderNameUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun editName(
        oldFolder: Folder,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newFolder = oldFolder.copy(name = name)

        folderRepository.edit(
            oldData = oldFolder,
            newData = newFolder,
            uid = uid,
            onFail = onFail
        )
    }
}
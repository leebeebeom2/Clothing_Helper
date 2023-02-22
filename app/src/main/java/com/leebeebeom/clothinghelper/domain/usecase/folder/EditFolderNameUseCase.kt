package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class EditFolderNameUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun edit(
        oldFolder: Folder,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newFolder = oldFolder.copy(name = name, editDate = System.currentTimeMillis(), isSynced = false)

        folderRepository.edit(newData = newFolder, uid = uid, onFail = onFail)
    }
}
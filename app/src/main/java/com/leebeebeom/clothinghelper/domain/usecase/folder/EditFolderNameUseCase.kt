package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class EditFolderNameUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun editName(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        oldFolder: Folder,
        name: String,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val newFolder = oldFolder.copy(name = name)

        folderRepository.edit(
            newData = newFolder,
            uid = uid,
            onFail = onFail,
            dispatcher = dispatcher
        )
    }
}
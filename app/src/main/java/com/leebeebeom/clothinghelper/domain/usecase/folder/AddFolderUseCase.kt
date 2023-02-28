package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.toDatabaseModel
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(
        parentKey: String,
        subCategoryKey: String,
        name: String,
        mainCategoryType: MainCategoryType,
        uid: String,
        onFail: (Exception) -> Unit,
    ) {
        val folder = Folder(
            parentKey = parentKey,
            subCategoryKey = subCategoryKey,
            name = name,
            mainCategoryType = mainCategoryType
        )

        folderRepository.add(data = folder.toDatabaseModel(), uid = uid, onFail = onFail)
    }
}
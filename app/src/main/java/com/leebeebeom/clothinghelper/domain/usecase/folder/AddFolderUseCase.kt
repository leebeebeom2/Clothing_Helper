package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.ui.drawer.contents.MainCategoryType
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(
        parentKey: String,
        subCategoryKey: String,
        name: String,
        mainCategoryType: MainCategoryType,
    ) = folderRepository.add(
        data = Folder(
            parentKey = parentKey,
            subCategoryKey = subCategoryKey,
            name = name,
            mainCategoryType = mainCategoryType
        )
    )
}
package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.Folder
import com.leebeebeom.clothinghelper.domain.model.MenuType
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(parentKey: String, name: String, menuType: MenuType) =
        folderRepository.add(data = Folder(parentKey = parentKey, name = name, menuType = menuType))
}
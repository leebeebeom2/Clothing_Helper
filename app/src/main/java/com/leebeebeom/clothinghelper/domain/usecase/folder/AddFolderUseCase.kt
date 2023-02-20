package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.model.FirebaseResult
import com.leebeebeom.clothinghelper.domain.model.data.Folder
import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import com.leebeebeom.clothinghelper.ui.main.drawer.MainCategoryType
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(
        parentKey: String,
        subCategoryKey: String,
        name: String,
        mainCategoryType: MainCategoryType,
        uid: String,
    ): FirebaseResult {
        val currentTime = System.currentTimeMillis()

        val folder = Folder(
            parentKey = parentKey,
            subCategoryKey = subCategoryKey,
            name = name,
            parent = mainCategoryType,
            createDate = currentTime,
            editDate = currentTime
        )

        return folderRepository.add(data = folder, uid = uid)
    }
}
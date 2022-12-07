package com.leebeebeom.clothinghelperdomain.usecase.folder

import com.leebeebeom.clothinghelperdomain.model.FirebaseResult
import com.leebeebeom.clothinghelperdomain.model.data.Folder
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(
        parentKey: String,
        subCategoryKey: String,
        name: String,
        subCategoryParent: SubCategoryParent,
        uid: String
    ): FirebaseResult {
        val currentTime = System.currentTimeMillis()
        val folder = Folder(
            parentKey = parentKey,
            subCategoryKey = subCategoryKey,
            name = name,
            parent = subCategoryParent,
            createDate = currentTime,
            editDate = currentTime
        )

        return folderRepository.add(t = folder, uid = uid)
    }
}
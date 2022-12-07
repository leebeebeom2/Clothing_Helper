package com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.folder

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.BaseContainerAddAndEdit
import com.leebeebeom.clothinghelperdomain.model.data.SubCategoryParent
import com.leebeebeom.clothinghelperdomain.usecase.folder.AddFolderUseCase

interface AddFolder : BaseContainerAddAndEdit {
    val addFoldersUseCase: AddFolderUseCase

    suspend fun baseAddFolder(
        parentKey: String,
        subCategoryKey: String,
        name: String,
        subCategoryParent: SubCategoryParent,
    ) {
        uid?.let {
            val result = addFoldersUseCase.add(
                parentKey = parentKey,
                subCategoryKey = subCategoryKey,
                name = name,
                subCategoryParent = subCategoryParent,
                uid = it
            )
            showFailToast(
                result = result,
                networkFailError = R.string.network_error_for_add_folder,
                failError = R.string.add_folder_failed
            )
        }
    }
}
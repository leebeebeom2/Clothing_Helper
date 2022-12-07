package com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.folder

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.main.base.interfaces.addandedit.BaseContainerAddAndEdit
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toUnstable
import com.leebeebeom.clothinghelperdomain.usecase.folder.EditFolderNameUseCase

interface EditFolder : BaseContainerAddAndEdit {
    val editFolderNameUseCase: EditFolderNameUseCase

    suspend fun baseEditFolder(folder: StableFolder) {
        uid?.let {
            val result = editFolderNameUseCase.edit(newFolder = folder.toUnstable(), uid = it)
            showFailToast(
                result = result,
                networkFailError = R.string.network_error_for_edit_folder,
                failError = R.string.edit_folder_name_failed
            )
        }
    }
}
package com.leebeebeom.clothinghelper.main.detail.interfaces

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.AddAndEditContainer
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.usecase.folder.EditFolderNameUseCase

interface EditFolderName : AddAndEditContainer {
    val editFolderNameUseCase: EditFolderNameUseCase

    suspend fun baseEditFolderName(folder: StableFolder) {
        uid?.let {
            val result = editFolderNameUseCase.edit(folder.toStable(), it)
            showToastWhenFail(
                result,
                R.string.network_error_for_edit_folder,
                R.string.edit_folder_name_failed
            )
        }
    }
}
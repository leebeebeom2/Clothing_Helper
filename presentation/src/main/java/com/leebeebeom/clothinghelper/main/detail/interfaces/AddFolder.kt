package com.leebeebeom.clothinghelper.main.detail.interfaces

import com.leebeebeom.clothinghelper.R
import com.leebeebeom.clothinghelper.base.AddAndEditContainer
import com.leebeebeom.clothinghelper.map.StableFolder
import com.leebeebeom.clothinghelper.map.toStable
import com.leebeebeom.clothinghelperdomain.usecase.folder.AddFolderUseCase

interface AddFolder : AddAndEditContainer {
    val addFoldersUseCase: AddFolderUseCase

    suspend fun baseAddFolder(folder: StableFolder) {
        uid?.let {
            val result = addFoldersUseCase.add(folder.toStable(), it)
            showToastWhenFail(
                result,
                R.string.network_error_for_add_folder,
                R.string.add_folder_failed
            )
        }
    }
}
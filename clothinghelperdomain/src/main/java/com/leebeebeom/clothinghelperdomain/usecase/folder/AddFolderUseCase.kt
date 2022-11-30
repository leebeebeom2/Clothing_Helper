package com.leebeebeom.clothinghelperdomain.usecase.folder

import com.leebeebeom.clothinghelperdomain.model.data.Folder
import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class AddFolderUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    suspend fun add(folder: Folder, uid: String) = folderRepository.add(folder, uid)
}
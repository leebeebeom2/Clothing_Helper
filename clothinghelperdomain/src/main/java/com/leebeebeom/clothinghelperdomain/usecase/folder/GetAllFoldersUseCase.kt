package com.leebeebeom.clothinghelperdomain.usecase.folder

import com.leebeebeom.clothinghelperdomain.repository.FolderRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAllFoldersUseCase @Inject constructor(folderRepository: FolderRepository) {
    val allFolders = folderRepository.allFolders
}
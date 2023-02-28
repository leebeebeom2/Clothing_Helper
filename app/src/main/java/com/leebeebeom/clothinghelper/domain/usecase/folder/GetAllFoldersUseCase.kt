package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

class GetAllFoldersUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val allFolders get() = folderRepository.allData
}
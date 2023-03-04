package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllFoldersUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val allFolders get() = folderRepository.allData
}
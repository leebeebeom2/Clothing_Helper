package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetIsFolderLoadingStateUserCase @Inject constructor(private val folderRepository: FolderRepository) {
    val isLoading get() = folderRepository.isLoading
}
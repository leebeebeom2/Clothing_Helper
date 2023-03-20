package com.leebeebeom.clothinghelper.domain.usecase.folder

import com.leebeebeom.clothinghelper.domain.repository.FolderRepository
import javax.inject.Inject

class GetFolderLoadingStreamUseCase @Inject constructor(private val folderRepository: FolderRepository) {
    val folderLoadingStream get() = folderRepository.loadingStream
}
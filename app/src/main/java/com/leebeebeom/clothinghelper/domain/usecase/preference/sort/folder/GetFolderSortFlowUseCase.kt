package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.folder

import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import javax.inject.Inject

class GetFolderSortFlowUseCase @Inject constructor(private val folderPreferenceRepository: FolderPreferenceRepository) {
    val folderSort get() = folderPreferenceRepository.sortFlow
}
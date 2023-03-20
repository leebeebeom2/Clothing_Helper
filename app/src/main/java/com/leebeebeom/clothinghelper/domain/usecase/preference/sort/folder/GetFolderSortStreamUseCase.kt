package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.folder

import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import javax.inject.Inject

class GetFolderSortStreamUseCase @Inject constructor(@FolderPreferencesRepository private val sortPreferenceRepository: SortPreferenceRepository) {
    val folderSort get() = sortPreferenceRepository.sortStream
}
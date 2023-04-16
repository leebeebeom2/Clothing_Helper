package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.folder

import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import javax.inject.Inject

class ChangeFolderSortUseCase @Inject constructor(private val folderPreferenceRepository: FolderPreferenceRepository) {
    suspend fun changeSort(sort: Sort) = folderPreferenceRepository.changeSort(sort = sort)
}
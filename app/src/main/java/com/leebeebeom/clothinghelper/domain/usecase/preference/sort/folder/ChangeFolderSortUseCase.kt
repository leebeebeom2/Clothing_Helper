package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.folder

import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.usecase.preference.sort.BaseChangeSortUseCase
import javax.inject.Inject

class ChangeFolderSortUseCase @Inject constructor(@FolderPreferencesRepository sortPreferenceRepository: SortPreferenceRepository) :
    BaseChangeSortUseCase(sortPreferenceRepository)
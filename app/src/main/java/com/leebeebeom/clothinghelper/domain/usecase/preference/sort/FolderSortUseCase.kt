package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import javax.inject.Inject

class FolderSortUseCase @Inject constructor(@FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository) :
    SortUseCase(folderPreferencesRepository)
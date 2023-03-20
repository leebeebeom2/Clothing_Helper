package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.folder

import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.usecase.preference.sort.BaseChangeOrderUseCase
import javax.inject.Inject

class ChangeFolderOrderUseCase @Inject constructor(@FolderPreferencesRepository sortPreferenceRepository: SortPreferenceRepository) :
    BaseChangeOrderUseCase(sortPreferenceRepository)
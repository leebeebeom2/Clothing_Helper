package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferencesRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class FolderSortUseCase @Inject constructor(@FolderPreferencesRepository folderPreferencesRepository: SortPreferenceRepository) :
    SortUseCase(folderPreferencesRepository)
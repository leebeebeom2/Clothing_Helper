package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class SubCategorySortUseCase @Inject constructor(@SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository) :
    SortUseCase(subCategoryPreferencesRepository)
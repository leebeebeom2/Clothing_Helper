package com.leebeebeom.clothinghelper.domain.usecase.preference.sort

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategorySortUseCase @Inject constructor(@SubCategoryPreferencesRepository subCategoryPreferencesRepository: SortPreferenceRepository) :
    SortUseCase(subCategoryPreferencesRepository)
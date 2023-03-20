package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.subcategory

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import com.leebeebeom.clothinghelper.domain.usecase.preference.sort.BaseChangeSortUseCase
import javax.inject.Inject

class ChangeSubCategorySortUseCase @Inject constructor(@SubCategoryPreferencesRepository sortPreferenceRepository: SortPreferenceRepository) :
    BaseChangeSortUseCase(sortPreferenceRepository)
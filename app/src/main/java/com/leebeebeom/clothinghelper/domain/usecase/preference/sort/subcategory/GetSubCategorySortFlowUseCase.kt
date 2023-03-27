package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.subcategory

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import javax.inject.Inject

class GetSubCategorySortFlowUseCase @Inject constructor(@SubCategoryPreferencesRepository private val sortPreferenceRepository: SortPreferenceRepository) {
    val subCategorySortFlow get() = sortPreferenceRepository.sortFlow
}
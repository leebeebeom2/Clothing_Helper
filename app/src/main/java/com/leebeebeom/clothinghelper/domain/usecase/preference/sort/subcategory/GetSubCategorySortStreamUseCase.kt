package com.leebeebeom.clothinghelper.domain.usecase.preference.sort.subcategory

import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import com.leebeebeom.clothinghelper.domain.repository.preference.SubCategoryPreferencesRepository
import javax.inject.Inject

class GetSubCategorySortStreamUseCase @Inject constructor(@SubCategoryPreferencesRepository private val sortPreferenceRepository: SortPreferenceRepository) {
    val subCategorySortStream get() = sortPreferenceRepository.sortStream
}
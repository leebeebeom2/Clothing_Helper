package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository

class ToggleAllExpandUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke() = subCategoryPreferencesRepository.toggleAllExpand()
}

class GetSubCategoryPreferencesUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    operator fun invoke() = subCategoryPreferencesRepository.subCategorySort
}
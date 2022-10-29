package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.stateIn

class ToggleAllExpandUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke() = subCategoryPreferencesRepository.toggleAllExpand()
}

class GetSubCategoryPreferencesUseCase(private val subCategoryPreferencesRepository: SubCategoryPreferencesRepository) {
    suspend operator fun invoke(scope: CoroutineScope) =
        subCategoryPreferencesRepository.subCategoryPreferences.stateIn(scope)
}
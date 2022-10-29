package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.stateIn

class ToggleAllExpandUseCase(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke() = preferencesRepository.toggleAllExpand()
}

class GetSubCategoryPreferencesUseCase(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(scope: CoroutineScope) =
        preferencesRepository.subCategoryPreferences.stateIn(scope)
}
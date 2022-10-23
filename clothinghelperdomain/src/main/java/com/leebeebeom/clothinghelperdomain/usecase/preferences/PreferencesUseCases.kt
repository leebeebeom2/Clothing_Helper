package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.stateIn

class ToggleAllExpandUseCase(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke() = preferencesRepository.toggleAllExpand()
}

class GetPreferencesUseCase(private val preferencesRepository: PreferencesRepository) {
    suspend operator fun invoke(scope: CoroutineScope) =
        preferencesRepository.preferencesFlow.stateIn(scope)
}

class GetPreferencesAndToggleAllExpandUseCase(
    private val getPreferencesUseCase: GetPreferencesUseCase,
    private val toggleAllExpandUseCase: ToggleAllExpandUseCase
) {
    suspend fun getPreferences(scope: CoroutineScope) = getPreferencesUseCase(scope)
    suspend fun toggleAllExpand() = toggleAllExpandUseCase()
}
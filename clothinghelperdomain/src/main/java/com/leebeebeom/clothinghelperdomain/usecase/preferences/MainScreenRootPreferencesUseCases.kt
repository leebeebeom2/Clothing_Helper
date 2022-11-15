package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.MainScreenRootPreferencesRepository

class MainScreenRootAllExpandUseCase(private val mainScreenRootPreferencesRepository: MainScreenRootPreferencesRepository) {
    val isAllExpanded get() = mainScreenRootPreferencesRepository.isAllExpanded

    suspend fun toggleAllExpand() = mainScreenRootPreferencesRepository.toggleAllExpand()
}
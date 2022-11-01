package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.MainScreenRootPreferencesRepository

class MainScreenRootAllExpandUseCase(private val mainScreenRootPreferencesRepository: MainScreenRootPreferencesRepository) {
    val isAllExpand get() = mainScreenRootPreferencesRepository.isAllExpand

    suspend fun toggleAllExpand() = mainScreenRootPreferencesRepository.toggleAllExpand()
}
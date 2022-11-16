package com.leebeebeom.clothinghelperdomain.usecase.preferences

import com.leebeebeom.clothinghelperdomain.repository.MainScreenRootPreferencesRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class MainRootAllExpandUseCase @Inject constructor(private val mainScreenRootPreferencesRepository: MainScreenRootPreferencesRepository) {
    val isAllExpanded get() = mainScreenRootPreferencesRepository.isAllExpanded

    suspend fun toggleAllExpand() = mainScreenRootPreferencesRepository.toggleAllExpand()
}
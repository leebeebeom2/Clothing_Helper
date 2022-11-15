package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.Flow

interface MainScreenRootPreferencesRepository {
    val isAllExpanded: Flow<Boolean>

    suspend fun toggleAllExpand()
}
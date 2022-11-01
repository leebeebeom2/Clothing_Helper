package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.Flow

interface MainScreenRootPreferencesRepository {
    val isAllExpand: Flow<Boolean>

    suspend fun toggleAllExpand()
}
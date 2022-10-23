package com.leebeebeom.clothinghelperdomain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val preferencesFlow: Flow<UserPreferences>
    suspend fun toggleAllExpand()
}

data class UserPreferences(val allExpand: Boolean)
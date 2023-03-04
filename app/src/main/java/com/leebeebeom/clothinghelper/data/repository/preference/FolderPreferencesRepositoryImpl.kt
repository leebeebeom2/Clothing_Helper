package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelper.di.AppScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @AppScope appScope: CoroutineScope,
) : SortPreferenceRepositoryImpl(dataStore = context.folderDatastore, appScope = appScope)

private const val FOLDER = "folder preferences"
private val Context.folderDatastore by preferencesDataStore(name = FOLDER)
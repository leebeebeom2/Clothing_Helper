package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderPreferencesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    SortPreferenceRepositoryImpl(dataStore = context.folderDatastore)

private const val FOLDER = "folder preferences"
private val Context.folderDatastore by preferencesDataStore(name = FOLDER)
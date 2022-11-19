package com.leebeebeom.clothinghelperdata.repository.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdomain.repository.preferences.FolderPreferencesRepository
import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderPreferencesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    SortPreferenceRepository by SortPreferenceRepositoryImpl(context.folderDatastore),
    FolderPreferencesRepository

private const val FOLDER = "folder_preferences"
private val Context.folderDatastore by preferencesDataStore(name = FOLDER)
package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryPreferencesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    SortPreferenceRepositoryImpl(dataStore = context.subCategoryDatastore)

private const val SUBCATEGORY = "subCategory preferences"
private val Context.subCategoryDatastore by preferencesDataStore(name = SUBCATEGORY)
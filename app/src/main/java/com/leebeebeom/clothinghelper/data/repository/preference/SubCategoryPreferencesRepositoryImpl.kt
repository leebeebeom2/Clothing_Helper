package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelper.di.AppScope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @AppScope appScope: CoroutineScope,
) : SortPreferenceRepositoryImpl(dataStore = context.subCategoryDatastore, appScope = appScope)

private const val SUBCATEGORY = "subCategory preferences"
private val Context.subCategoryDatastore by preferencesDataStore(name = SUBCATEGORY)
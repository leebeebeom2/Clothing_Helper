package com.leebeebeom.clothinghelperdata.repository.preferences

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdomain.repository.preferences.SubCategoryPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryPreferencesRepositoryImpl @Inject constructor(@ApplicationContext context: Context) :
    BaseSortPreferencesRepositoryImpl(context.subCategoryDatastore),
    SubCategoryPreferencesRepository

private const val SUBCATEGORY = "subCategory_preferences"
private val Context.subCategoryDatastore by preferencesDataStore(name = SUBCATEGORY)
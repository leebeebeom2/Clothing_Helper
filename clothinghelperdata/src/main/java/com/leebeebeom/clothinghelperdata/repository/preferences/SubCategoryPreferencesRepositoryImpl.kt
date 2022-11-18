package com.leebeebeom.clothinghelperdata.repository.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.repository.SubCategoryPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubCategoryPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val dataStore: DataStore<Preferences> = context.subCategoryDatastore
) : SubCategoryPreferencesRepository {

    override val sort: Flow<SortPreferences> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val sort = it[SubCategoryPreferenceKeys.SORT] ?: Sort.NAME.name
            val order = it[SubCategoryPreferenceKeys.ORDER] ?: Order.ASCENDING.name
            SortPreferences(enumValueOf(sort), enumValueOf(order))
        }

    override suspend fun changeSort(sort: Sort) {
        dataStore.edit {
            it[SubCategoryPreferenceKeys.SORT] = sort.name
        }
    }

    override suspend fun changeOrder(order: Order) {
        dataStore.edit {
            it[SubCategoryPreferenceKeys.ORDER] = order.name
        }
    }
}

private object SubCategoryPreferenceKeys {
    val SORT = stringPreferencesKey("sort")
    val ORDER = stringPreferencesKey("order")
}

private const val SUBCATEGORY = "subCategory_preferences"
private val Context.subCategoryDatastore by preferencesDataStore(name = SUBCATEGORY)
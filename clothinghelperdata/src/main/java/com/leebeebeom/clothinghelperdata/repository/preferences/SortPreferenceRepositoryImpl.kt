package com.leebeebeom.clothinghelperdata.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
import com.leebeebeom.clothinghelperdomain.repository.preferences.SortPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SortPreferenceRepositoryImpl(private val dataStore: DataStore<Preferences>) : SortPreferenceRepository{
    override val sort: Flow<SortPreferences> = dataStore.data
        .catch {
            if (it is IOException) emit(emptyPreferences())
            else throw it
        }
        .map {
            val sort = it[SortPreferenceKeys.SORT] ?: Sort.NAME.name
            val order = it[SortPreferenceKeys.ORDER] ?: Order.ASCENDING.name
            SortPreferences(enumValueOf(sort), enumValueOf(order))
        }

    override suspend fun changeSort(sort: Sort) {
        dataStore.edit {
            it[SortPreferenceKeys.SORT] = sort.name
        }
    }

    override suspend fun changeOrder(order: Order) {
        dataStore.edit {
            it[SortPreferenceKeys.ORDER] = order.name
        }
    }

    private object SortPreferenceKeys {
        val SORT = stringPreferencesKey("sort")
        val ORDER = stringPreferencesKey("order")
    }
}
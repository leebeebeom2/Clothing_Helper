package com.leebeebeom.clothinghelper.data.repository.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leebeebeom.clothinghelper.domain.model.Order
import com.leebeebeom.clothinghelper.domain.model.Sort
import com.leebeebeom.clothinghelper.domain.model.SortPreferences
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

abstract class SortPreferenceRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    SortPreferenceRepository {

    override val sort: Flow<SortPreferences> = dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map {
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
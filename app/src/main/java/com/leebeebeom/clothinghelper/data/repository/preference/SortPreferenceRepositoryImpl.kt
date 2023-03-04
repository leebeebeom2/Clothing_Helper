package com.leebeebeom.clothinghelper.data.repository.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.leebeebeom.clothinghelper.domain.repository.preference.SortPreferenceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.io.IOException

abstract class SortPreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    appScope: CoroutineScope,
) : SortPreferenceRepository {

    override val sort = dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map {
        val sort = it[SortPreferenceKeys.SORT] ?: Sort.NAME.name
        val order = it[SortPreferenceKeys.ORDER] ?: Order.ASCENDING.name
        SortPreferences(sort = enumValueOf(sort), order = enumValueOf(order))
    }.stateIn(appScope, SharingStarted.WhileSubscribed(5000), SortPreferences())

    override suspend fun changeSort(sort: Sort) {
        dataStore.edit { it[SortPreferenceKeys.SORT] = sort.name }
    }

    override suspend fun changeOrder(order: Order) {
        dataStore.edit { it[SortPreferenceKeys.ORDER] = order.name }
    }

    private object SortPreferenceKeys {
        val SORT = stringPreferencesKey("sort")
        val ORDER = stringPreferencesKey("order")
    }
}

enum class Sort {
    NAME, CREATE, EDIT
}

enum class Order {
    ASCENDING, DESCENDING
}

data class SortPreferences(
    val sort: Sort = Sort.NAME,
    val order: Order = Order.ASCENDING,
)
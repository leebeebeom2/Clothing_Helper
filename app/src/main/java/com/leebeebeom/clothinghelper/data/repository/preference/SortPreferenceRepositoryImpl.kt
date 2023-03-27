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
import kotlinx.coroutines.flow.shareIn
import java.io.IOException

abstract class SortPreferenceRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    appScope: CoroutineScope,
) : SortPreferenceRepository {

    override val sortFlow = dataStore.data.catch {
        if (it is IOException) emit(emptyPreferences()) else throw it
    }.map {
        val sort = it[SortPreferenceKeys.sort] ?: Sort.Name.name
        val order = it[SortPreferenceKeys.order] ?: Order.Ascending.name
        SortPreferences(sort = enumValueOf(sort), order = enumValueOf(order))
    }.shareIn(scope = appScope, started = SharingStarted.WhileSubscribed(5000), replay = 1)

    override suspend fun changeSort(sort: Sort) {
        dataStore.edit { it[SortPreferenceKeys.sort] = sort.name }
    }

    override suspend fun changeOrder(order: Order) {
        dataStore.edit { it[SortPreferenceKeys.order] = order.name }
    }

    private object SortPreferenceKeys {
        val sort = stringPreferencesKey("sort")
        val order = stringPreferencesKey("order")
    }
}

enum class Sort {
    Name, Create, Edit
}

enum class Order {
    Ascending, Descending
}

data class SortPreferences(
    val sort: Sort = Sort.Name,
    val order: Order = Order.Ascending,
)
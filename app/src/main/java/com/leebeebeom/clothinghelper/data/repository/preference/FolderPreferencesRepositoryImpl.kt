package com.leebeebeom.clothinghelper.data.repository.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.leebeebeom.clothinghelper.di.AppScope
import com.leebeebeom.clothinghelper.domain.repository.preference.FolderPreferenceRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FolderPreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    @AppScope appScope: CoroutineScope,
) : FolderPreferenceRepository {
    private val folderDataStore = context.folderDatastore

    override val sortFlow = folderDataStore.getFlow(appScope) {
        val sort = it[SortPreferenceKeys.sort] ?: Sort.Name.name
        val order = it[SortPreferenceKeys.order] ?: Order.Ascending.name
        SortPreferences(sort = enumValueOf(sort), order = enumValueOf(order))
    }

    override suspend fun changeSort(sort: Sort) {
        folderDataStore.edit { it[SortPreferenceKeys.sort] = sort.name }
    }

    override suspend fun changeOrder(order: Order) {
        folderDataStore.edit { it[SortPreferenceKeys.order] = order.name }
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

private const val FolderDatastoreKey = "folder data store"
private val Context.folderDatastore by preferencesDataStore(name = FolderDatastoreKey)
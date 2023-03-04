package com.leebeebeom.clothinghelper.domain.repository.preference

import com.leebeebeom.clothinghelper.data.repository.preference.Order
import com.leebeebeom.clothinghelper.data.repository.preference.Sort
import com.leebeebeom.clothinghelper.data.repository.preference.SortPreferences
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Qualifier

interface SortPreferenceRepository {
    val sort: StateFlow<SortPreferences>

    suspend fun changeSort(sort: Sort)
    suspend fun changeOrder(order: Order)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCategoryPreferencesRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FolderPreferencesRepository
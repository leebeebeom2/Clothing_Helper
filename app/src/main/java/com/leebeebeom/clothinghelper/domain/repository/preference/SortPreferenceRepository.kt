package com.leebeebeom.clothinghelper.domain.repository.preference

import com.leebeebeom.clothinghelper.domain.model.Order
import com.leebeebeom.clothinghelper.domain.model.Sort
import com.leebeebeom.clothinghelper.domain.model.SortPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Qualifier

interface SortPreferenceRepository {
    val sort: Flow<SortPreferences>

    suspend fun changeSort(sort: Sort)
    suspend fun changeOrder(order: Order)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class SubCategoryPreferencesRepository

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class FolderPreferencesRepository
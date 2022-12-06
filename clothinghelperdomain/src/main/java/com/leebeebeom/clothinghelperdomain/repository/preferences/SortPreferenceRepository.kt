package com.leebeebeom.clothinghelperdomain.repository.preferences

import com.leebeebeom.clothinghelperdomain.model.Order
import com.leebeebeom.clothinghelperdomain.model.Sort
import com.leebeebeom.clothinghelperdomain.model.SortPreferences
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